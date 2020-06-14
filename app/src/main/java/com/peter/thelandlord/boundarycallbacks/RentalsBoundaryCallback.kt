package com.peter.thelandlord.boundarycallbacks

import android.util.Log
import androidx.paging.PagedList
import androidx.paging.PagingRequestHelper
import com.google.firebase.firestore.ktx.toObjects
import com.peter.thelandlord.data.apis.RentalsApi
import com.peter.thelandlord.domain.models.Rental
import com.peter.thelandlord.extensions.pagingrequesthelperextensions.createNetworkStatusLiveData
import com.peter.thelandlord.utils.Executors

class RentalsBoundaryCallback(
    val pageSize: Int,
    val propertyId: String,
    val handleRentalInsertion: (List<Rental>) -> Unit,
    val rentalsApi: RentalsApi
): PagedList.BoundaryCallback<Rental>() {

    private val executor = Executors.executor
    val helper = PagingRequestHelper(executor)
    val networkState = helper.createNetworkStatusLiveData()

    companion object {
        const val TAG = "RENTAL_BOUND_CLBCK"
    }

    override fun onZeroItemsLoaded() {
        super.onZeroItemsLoaded()

        helper.runIfNotRunning(PagingRequestHelper.RequestType.INITIAL){
            loadAndCacheRentals()
        }
    }

    override fun onItemAtEndLoaded(itemAtEnd: Rental) {
        super.onItemAtEndLoaded(itemAtEnd)

        helper.runIfNotRunning(PagingRequestHelper.RequestType.AFTER){
            loadAndCacheRentals(itemAtEnd)
        }

    }

    private fun loadAndCacheRentals(itemAtEnd: Rental? = null){

        if (itemAtEnd == null){
            rentalsApi.getRentalsInitial(propertyId, pageSize)
            .addOnSuccessListener {
                Executors.ioExecutor {
                    val rentals = it.toObjects<Rental>()
                    handleRentalInsertion(rentals)
                }
            }
            .addOnFailureListener {
                Log.d(TAG, "${it.message}")
            }
        }else{
            rentalsApi.getRentalsAfter(propertyId, pageSize, itemAtEnd)
            .addOnSuccessListener {
                Executors.ioExecutor {
                    val rentals = it.toObjects<Rental>()
                    handleRentalInsertion(rentals)
                }
            }
            .addOnFailureListener {
                Log.d(TAG, "${it.message}")
            }
        }
    }
}