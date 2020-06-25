package com.peter.thelandlord.data.apis

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.WriteBatch
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.peter.thelandlord.domain.models.Payment
import com.peter.thelandlord.utils.DebtFields
import com.peter.thelandlord.utils.FirestoreCollections
import com.peter.thelandlord.utils.PaymentFields
import com.peter.thelandlord.utils.RentalAccountSummaryFields

object RentalAccountApi {
    private val firestore = Firebase.firestore
    private val TAG = "RentalAccApi"

    fun getAllDebtsForRental(rentalId: String): Task<QuerySnapshot> {
        return firestore.collection(FirestoreCollections.DEBTS)
            .whereEqualTo(DebtFields.RENTAL_ID, rentalId)
            .get()
    }

    fun getAllDebtsForProperty(propertyId: String): Task<QuerySnapshot> {
        return firestore.collection(FirestoreCollections.DEBTS)
            .whereEqualTo(DebtFields.RENTAL_ID, propertyId)
            .get()
    }

    fun getRentalAccountSummary(rentalId: String): Task<QuerySnapshot>{
        return firestore.collection(FirestoreCollections.RENTAL_ACCOUNT_SUMMARY)
            .whereEqualTo(RentalAccountSummaryFields.RENTAL_ID, rentalId)
            .get()
    }

    fun getPaymentsInitial(propertyId: String, limit: Int): Task<QuerySnapshot>{
        Log.d(TAG, "${PaymentFields.PROPERTY_ID}, $propertyId")

        return firestore.collection(FirestoreCollections.PAYMENTS)
            .whereEqualTo(PaymentFields.PROPERTY_ID, propertyId)
            .limit(limit.toLong())
            .orderBy(PaymentFields.TIMESTAMP, Query.Direction.ASCENDING)
            .get()
    }

    fun getPaymentsAfter(propertyId: String, timestamp: String, limit: Int ): Task<QuerySnapshot>{
        Log.d(TAG, "Property Id, $propertyId")

        return firestore.collection(FirestoreCollections.PAYMENTS)
            .whereEqualTo(PaymentFields.PROPERTY_ID, propertyId)
            .whereGreaterThan(PaymentFields.TIMESTAMP, timestamp)
            .limit(limit.toLong())
            .orderBy(PaymentFields.TIMESTAMP, Query.Direction.ASCENDING)
            .get()
    }

    fun getSearchPaymentsInitial(propertyId: String, limit: Int): Task<QuerySnapshot>{
//        return firestore.collection(FirestoreCollections.PAYMENTS)
//            .where
        // TODO: 10/06/2020
        TODO()
    }

    fun getSearchPaymentsAfter(propertyId: String, timestamp: String, limit: Int ): Task<QuerySnapshot>{
        TODO()
    }

    fun uploadPayments(payments: List<Payment>): Task<Void>{
        val batch: WriteBatch = firestore.batch()
        val ref = firestore.collection(FirestoreCollections.PAYMENTS)

        payments.forEach {
            batch.set(ref.document(it.paymentId), it)
        }

        return batch.commit()
    }

}

