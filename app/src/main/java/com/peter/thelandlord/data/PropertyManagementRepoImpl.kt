package com.peter.thelandlord.data

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.peter.thelandlord.domain.interfaces.PropertyManagementRepo
import com.peter.thelandlord.domain.models.Property
import com.peter.thelandlord.domain.objects.FirestoreCollections

class PropertyManagementRepoImpl (val firestore: FirebaseFirestore, val firebaseAuth: FirebaseAuth) :
    PropertyManagementRepo {

    override fun saveProperty(
        property: Property,
        isSavingLiveData: MutableLiveData<Boolean>,
        errorLiveData: MutableLiveData<String>,
        successLiveData: MutableLiveData<String>
    ) {
        val propertyID = firestore.collection(FirestoreCollections.PROPERTIES).document().id

        property.owner = firebaseAuth.currentUser?.email!!
        property.propertyID = propertyID

        firestore.collection(FirestoreCollections.PROPERTIES)
            .document(propertyID).set(property)
            .addOnSuccessListener {
                successLiveData.postValue("Property saved successfully")
            }
            .addOnFailureListener {
                errorLiveData.postValue(it.message)
            }
            .addOnCompleteListener {
                isSavingLiveData.postValue(false)
            }
    }
}