package com.peter.thelandlord.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import androidx.paging.Config
import androidx.paging.LivePagedListBuilder
import androidx.work.*
import com.peter.thelandlord.utils.Utils.getCurrentTimestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObjects
import com.peter.thelandlord.boundarycallbacks.RentalsBoundaryCallback
import com.peter.thelandlord.data.apis.RentalsApi
import com.peter.thelandlord.data.db.AppDatabase
import com.peter.thelandlord.data.listing.Listing
import com.peter.thelandlord.data.networkstate.NetworkState
import com.peter.thelandlord.domain.interfaces.RentalManagementRepo
import com.peter.thelandlord.domain.models.Property
import com.peter.thelandlord.domain.models.Rental
import com.peter.thelandlord.utils.Constants
import com.peter.thelandlord.utils.FirestoreCollections
import com.peter.thelandlord.work.UploadRentalWorker
import io.reactivex.Completable
import java.util.concurrent.Executors

class RentalManagementRepoImpl (val firestore: FirebaseFirestore, val appDb: AppDatabase, val workManager: WorkManager) : RentalManagementRepo {
    private val executor = Executors.newSingleThreadExecutor()
    private val rentalsApi = RentalsApi()
    private val rentalDao = appDb.rentalDao()
    private val propertyDao = appDb.propertyDao()

    companion object{
        const val TAG = "RENTAL_MANGT_REPO"
        const val DB_PAGE_SIZE = 10
        const val NETWORK_PAGE_SIZE = 20
    }

    override fun saveRental(rental: Rental ): Completable {

        rental.timestamp = getCurrentTimestamp()
        rental.id = firestore.collection(FirestoreCollections.RENTALS).document().id

        val data = workDataOf(Constants.KEY_RENTAL_ID to rental.rentalNumber)

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val workRequest = OneTimeWorkRequestBuilder<UploadRentalWorker>()
            .setInputData(data)
            .setConstraints(constraints)
            .build()

        workManager.enqueue(workRequest)

        return rentalDao.saveRental(rental)
    }

    private fun insertRentalsIntoDb(rentals: List<Rental>){
        rentalDao.insertRentals(rentals)
    }

    private fun refresh(propertyId: String): LiveData<NetworkState>{
        val refreshState = MutableLiveData<NetworkState>()
        refreshState.value = NetworkState.LOADING

        rentalsApi.getRentalsInitial(propertyId, NETWORK_PAGE_SIZE)
            .addOnSuccessListener {
                snapshot ->
                ioThread {
                    appDb.runInTransaction {
                        rentalDao.deleteRentalByPropertyId(propertyId)
                        val rentals = snapshot.toObjects<Rental>()
                        rentalDao.insertRentals(rentals)
                        refreshState.postValue(NetworkState.LOADED)
                    }
                }
            }
            .addOnFailureListener {
                refreshState.value = NetworkState.error("Error while refreshing: ${it.message}")
            }

        return refreshState
    }



    override fun rentalsFromProperty(propertyId: String): Listing<Rental> {

        val boundaryCallback = RentalsBoundaryCallback(
            pageSize = NETWORK_PAGE_SIZE,
            propertyId = propertyId,
            handleRentalInsertion = this::insertRentalsIntoDb,
            rentalsApi = rentalsApi
        )
        val dataSourceFactory = rentalDao.getRentalsByProperty(propertyId)
        val config = Config(
            pageSize = DB_PAGE_SIZE
        )
        val livePagedList = LivePagedListBuilder(dataSourceFactory, config)
            .setBoundaryCallback(boundaryCallback)
            .build()

        val refreshTrigger = MutableLiveData<Unit>()
        val refreshState = refreshTrigger.switchMap {
            refresh(propertyId)
        }

        return Listing (
            livePagedList = livePagedList,
            networkState = boundaryCallback.networkState,
            refreshState = refreshState,
            refresh = {
                refreshTrigger.value = null
            },
            retry = {
                boundaryCallback.helper.retryAllFailed()
            }
        )
    }

    override fun getRentalLiveData(rentalId: String): LiveData<Rental> = rentalDao.getRentalByIdLiveData(rentalId)

    override fun getPropertyDetailsForRental(propertyId: String): LiveData<Property> = propertyDao.getPropertyById(propertyId)

    private fun ioThread(f: () -> Unit){
        executor.execute(f)
    }


}