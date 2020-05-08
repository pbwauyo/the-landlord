package com.peter.thelandlord.data.apis

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.peter.thelandlord.domain.models.Rental
import com.peter.thelandlord.utils.FirestoreCollections
import com.peter.thelandlord.utils.RentalFields

class RentalsApi {
    val firestore = Firebase.firestore

    fun getRentalsInitial(propertyId: String, pageSize: Int): Task<QuerySnapshot> {
        return firestore.collection(FirestoreCollections.RENTALS)
            .whereEqualTo(RentalFields.PROPERTY_ID, propertyId)
            .limit(pageSize.toLong())
            .orderBy(RentalFields.TIMESTAMP, Query.Direction.ASCENDING)
            .get()
    }

    fun getRentalsAfter(propertyId: String, pageSize: Int, itemAtEnd: Rental): Task<QuerySnapshot>{
        return firestore.collection(FirestoreCollections.RENTALS)
            .whereEqualTo(RentalFields.PROPERTY_ID, propertyId)
            .whereGreaterThan(RentalFields.TIMESTAMP, itemAtEnd.timestamp)
            .limit(pageSize.toLong())
            .orderBy(RentalFields.TIMESTAMP, Query.Direction.ASCENDING)
            .get();
    }

}