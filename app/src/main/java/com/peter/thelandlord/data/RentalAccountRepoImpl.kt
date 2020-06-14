package com.peter.thelandlord.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import androidx.paging.Config
import androidx.paging.LivePagedListBuilder
import androidx.work.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import com.peter.thelandlord.boundarycallbacks.PaymentsBoundaryCallback
import com.peter.thelandlord.data.apis.RentalAccountApi
import com.peter.thelandlord.data.db.AppDatabase
import com.peter.thelandlord.data.listing.Listing
import com.peter.thelandlord.data.networkstate.NetworkState
import com.peter.thelandlord.domain.interfaces.RentalAccountRepo
import com.peter.thelandlord.domain.models.Debt
import com.peter.thelandlord.domain.models.Payment
import com.peter.thelandlord.utils.Constants
import com.peter.thelandlord.utils.Executors
import com.peter.thelandlord.utils.FirestoreCollections
import com.peter.thelandlord.work.UploadPaymentsWorker
import io.reactivex.Completable
import java.util.concurrent.Executor
import javax.inject.Inject

class RentalAccountRepoImpl @Inject constructor (
    val appDatabase: AppDatabase,
    val workManager: WorkManager) :
    RentalAccountRepo {

    private val firestore = Firebase.firestore
    companion object {
        const val TAG = "RENTAL_ACC_REPO"
        const val DEFAULT_NETWORK_PAGE_SIZE = 15
        const val DB_PAGE_SIZE = 15
    }

    private val paymentDao = appDatabase.paymentDao()
    private val debtDao = appDatabase.debtDao()

    override fun getAllDebtsForProperty(propertyId: String): LiveData<List<Debt>> {

        RentalAccountApi.getAllDebtsForProperty(propertyId)
            .addOnFailureListener {
                Log.d(TAG, "DEBTS FOR PROPERTY, ${it.message}")
            }
            .addOnSuccessListener {
                val debts: List<Debt> = it.toObjects()
                Executors.ioExecutor {
                    val currentDbDebts = debtDao.getRentalDebts(propertyId)
                    debts.forEach { debt ->
                        if(!currentDbDebts.contains(debt)){
                            debtDao.saveDebt(debt)
                        }
                    }

                }
            }

        return debtDao.getPropertyDebtsLD(propertyId)
    }

    override fun getAllDebtsForRental(rentalId: String): LiveData<List<Debt>> {

        RentalAccountApi.getAllDebtsForRental(rentalId)
            .addOnFailureListener {
                Log.d(TAG, "DEBTS FOR RENTAL, ${it.message}")
            }
            .addOnSuccessListener {
                val debts: List<Debt> = it.toObjects()
                Executors.ioExecutor {
                    val currentDbDebts = debtDao.getRentalDebts(rentalId)
                    debts.forEach { debt ->
                        if(!currentDbDebts.contains(debt)){
                            debtDao.saveDebt(debt)
                        }
                    }
                }
            }

        return debtDao.getRentalDebtsLD(rentalId)
    }

    override fun getAllPayments(propertyId: String): Listing<Payment> {
        TODO("Not yet implemented")
    }

    override fun getAllPaymentsByTimestamp(timestamp: String): List<Payment> {
        TODO("Not yet implemented")
    }

    override fun saveDebts(debts: List<Debt>){
        debtDao.saveDebts(debts)
    }

    override fun savePayments(payments: List<Payment>): Completable {

        val reference = firestore.collection(FirestoreCollections.PAYMENTS)
        payments.forEach {
            it.paymentId = reference.document().id
        }

        val data = workDataOf(
            Constants.KEY_TIMESTAMP to payments[0].timestamp
        )

        val constraints = Constraints.Builder()
             .setRequiredNetworkType(NetworkType.CONNECTED)
             .build()

        val uploadRequest = OneTimeWorkRequestBuilder<UploadPaymentsWorker>()
            .setInputData(data)
            .setConstraints(constraints)
            .build()

        workManager.enqueue(uploadRequest)

        return paymentDao.savePaymentsCompletable(payments)
    }

    //handle payments list fetch

    fun savePaymentsToDb(paymentsList: List<Payment>){
        paymentDao.savePayments(paymentsList)
    }

    fun refresh(propertyId: String): LiveData<NetworkState>{
        val networkState = MutableLiveData<NetworkState>()
        networkState.value = NetworkState.LOADING

        RentalAccountApi.getPaymentsInitial(propertyId, DEFAULT_NETWORK_PAGE_SIZE)
            .addOnSuccessListener {
                val payments = it.toObjects<Payment>()
                networkState.value = NetworkState.LOADED
                appDatabase.runInTransaction {
                    paymentDao.deleteAllPaymentsByPropertyId(propertyId)
                    paymentDao.savePayments(payments)
                }

            }
            .addOnFailureListener {
                networkState.value = NetworkState.error(it.message)
            }

        return networkState
    }

    fun handlePaymentsFetching(propertyId: String): Listing<Payment>{

        val boundaryCallback = PaymentsBoundaryCallback(
            propertyId = propertyId,
            limit = DEFAULT_NETWORK_PAGE_SIZE,
            searchText = "",
            handlePaymentsInsertion = this::savePaymentsToDb
        )

        val refreshTrigger = MutableLiveData<Unit>()
        val refreshState = refreshTrigger.switchMap {
            refresh(propertyId)
        }

        val config = Config(
            pageSize = DB_PAGE_SIZE
        )

        val dataSource = paymentDao.getAllPaymentsForPropertyLD(propertyId)

        val pagedList = LivePagedListBuilder(dataSource, config)
            .setBoundaryCallback(boundaryCallback)
            .build()

        return Listing(
            livePagedList = pagedList,
            networkState = boundaryCallback.networkState,
            refreshState = refreshState,
            retry = {
                boundaryCallback.helper.retryAllFailed()
            },
            refresh = {
                refreshTrigger.value = null
            }
        )
    }


}
