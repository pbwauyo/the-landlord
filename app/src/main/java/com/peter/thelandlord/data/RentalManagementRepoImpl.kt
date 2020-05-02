package com.peter.thelandlord.data

import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.peter.thelandlord.domain.interfaces.RentalManagementRepo
import com.peter.thelandlord.domain.models.Rental
import com.peter.thelandlord.objects.FirestoreCollections

class RentalManagementRepoImpl (val firestore: FirebaseFirestore) : RentalManagementRepo {

    override fun saveRental(
        rental: Rental,
        isSavingLiveData: MutableLiveData<Boolean>,
        errorLiveData: MutableLiveData<String>,
        successLiveData: MutableLiveData<String>
    ) {
        firestore.collection(FirestoreCollections.RENTALS).add(rental)
            .addOnSuccessListener {
                successLiveData.postValue("Rentals details saved successfully")
            }
            .addOnFailureListener {
                errorLiveData.postValue(it.message)
            }
            .addOnSuccessListener {
                isSavingLiveData.postValue(false)
            }
    }


}