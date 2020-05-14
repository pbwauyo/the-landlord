package com.peter.thelandlord.data.apis

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.peter.thelandlord.domain.models.Rental
import com.peter.thelandlord.domain.models.Tenant
import com.peter.thelandlord.utils.FirestoreCollections
import com.peter.thelandlord.utils.RentalFields

class RentalsApi {

    companion object{
        val firestore = Firebase.firestore
        const val TAG = "RENTALS_API"

        fun updateTenantDetails(tenant: Tenant, rentalId: String): Task<Void>{

            val updates = hashMapOf<String, Any>(
                RentalFields.TENANT_NAME to tenant.name,
                RentalFields.TENANT_CONTACT to tenant.contact,
                RentalFields.TENANCY_START_DATE to tenant.startDate
            )

            return firestore.collection(FirestoreCollections.RENTALS)
                .document(rentalId)
                .update(updates)
        }

        fun removeTenantDetails(rentalId: String): Task<Void>{
            val emptyString = ""

            val updates = hashMapOf<String, Any>(
                RentalFields.TENANT_NAME to emptyString,
                RentalFields.TENANT_CONTACT to emptyString,
                RentalFields.TENANCY_START_DATE to emptyString
            )

            return firestore.collection(FirestoreCollections.RENTALS)
                .document(rentalId)
                .update(updates)
        }
    }


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