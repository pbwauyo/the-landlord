package com.peter.thelandlord.boundarycallbacks

import android.util.Log
import androidx.paging.PagedList
import androidx.paging.PagingRequestHelper
import com.google.firebase.firestore.ktx.toObjects
import com.peter.thelandlord.data.apis.RentalAccountApi
import com.peter.thelandlord.data.dao.PaymentDao
import com.peter.thelandlord.domain.models.Payment
import com.peter.thelandlord.extensions.pagingrequesthelperextensions.createNetworkStatusLiveData
import com.peter.thelandlord.utils.Executors

class PaymentsBoundaryCallback(
    private val propertyId: String,
    private val limit: Int,
    private var searchText: String,
    private val handlePaymentsInsertion: (List<Payment>) -> Unit
) : PagedList.BoundaryCallback<Payment>() {

    companion object {
        private val TAG = "PAYTS_BOUND_CLBCK"

    }

    val helper = PagingRequestHelper(Executors.executor)
    val networkState = helper.createNetworkStatusLiveData()

    override fun onZeroItemsLoaded() {
        super.onZeroItemsLoaded()

        helper.runIfNotRunning(PagingRequestHelper.RequestType.INITIAL){
            RentalAccountApi.getPaymentsInitial(propertyId, limit)
                .addOnSuccessListener {
                    Executors.ioExecutor {
                        val paymentsList = it.toObjects<Payment>()
                        Log.d(TAG, "Payments initial, $paymentsList")
                        handlePaymentsInsertion(paymentsList)
                    }
                }
                .addOnFailureListener {
                    Log.d(TAG, "Payments Error initial, ${it.message}")
                }
        }
    }

    override fun onItemAtEndLoaded(itemAtEnd: Payment) {
        super.onItemAtEndLoaded(itemAtEnd)

        helper.runIfNotRunning(PagingRequestHelper.RequestType.AFTER){
            RentalAccountApi.getPaymentsAfter(propertyId, itemAtEnd.timestamp, limit)
                .addOnSuccessListener {
                    Executors.ioExecutor {
                        val paymentsList = it.toObjects<Payment>()
                        Log.d(TAG, "Payments after, $paymentsList")
                        handlePaymentsInsertion(paymentsList)
                    }
                }
                .addOnFailureListener {
                    Log.d(TAG, "Payments Error after, ${it.message}")
                }
        }
    }

}