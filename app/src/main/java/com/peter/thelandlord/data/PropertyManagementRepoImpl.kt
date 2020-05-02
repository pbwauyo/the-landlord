package com.peter.thelandlord.data

import android.util.Log
import androidx.work.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.peter.thelandlord.data.dao.PropertyDao
import com.peter.thelandlord.domain.interfaces.PropertyManagementRepo
import com.peter.thelandlord.domain.models.Property
import com.peter.thelandlord.objects.Constants
import com.peter.thelandlord.objects.FirestoreCollections
import com.peter.thelandlord.work.UploadPropertyToFirebaseWorker
import io.reactivex.Completable

class PropertyManagementRepoImpl (
    val firestore: FirebaseFirestore,
    val firebaseAuth: FirebaseAuth,
    val propertyDao: PropertyDao,
    val workManager: WorkManager) :
    PropertyManagementRepo {

    private companion object{
        val TAG = this::class.java.simpleName
    }

    override fun saveProperty(property: Property): Completable {
        val propertyID = firestore.collection(FirestoreCollections.PROPERTIES).document().id

        property.owner = firebaseAuth.currentUser?.email!!
        property.propertyID = propertyID

        val inputData = workDataOf(Constants.KEY_PROPERTY_ID to propertyID)
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val uploadRequest = OneTimeWorkRequestBuilder<UploadPropertyToFirebaseWorker>()
            .setInputData(inputData)
            .setConstraints(constraints)
            .build()

        workManager.enqueue(uploadRequest)

        return propertyDao.saveProperty(property)
    }

//    override fun saveProperty(
//        property: Property,
//        isSavingLiveData: MutableLiveData<Boolean>,
//        errorLiveData: MutableLiveData<String>,
//        successLiveData: MutableLiveData<String>
//    ) {
//        val propertyID = firestore.collection(FirestoreCollections.PROPERTIES).document().id
//
//        property.owner = firebaseAuth.currentUser?.email!!
//        property.propertyID = propertyID
//
//        propertyDao.saveProperty(property)
//                   .subscribe(
//                       {
//                           successLiveData.postValue("Property saved successfully")
//                           isSavingLiveData.postValue(false)
//                       },
//                       {
//                           errorLiveData.postValue(it.message)
//                           isSavingLiveData.postValue(false)
//                       }
//                   ).dispose()
//
//        firestore.collection(FirestoreCollections.PROPERTIES)
//            .document(propertyID).set(property)
//            .addOnSuccessListener {
//
//            }
//            .addOnFailureListener {
//
//            }
//            .addOnCompleteListener {
//
//            }
//    }
}