package com.peter.thelandlord.data.apis

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.WriteBatch
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.peter.thelandlord.domain.models.Payment
import com.peter.thelandlord.utils.DebtFields
import com.peter.thelandlord.utils.FirestoreCollections
import com.peter.thelandlord.utils.RentalAccountSummaryFields

object RentalAccountApi {
    private val firestore = Firebase.firestore

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

    fun getPaymentsInitial(propertyId: String): Task<QuerySnapshot>{
        TODO()
    }

    fun getPaymentsAfter(propertyId: String, start: Int, limit: Int ): Task<QuerySnapshot>{
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

