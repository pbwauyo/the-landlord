package com.peter.thelandlord.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.work.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.peter.thelandlord.data.dao.PropertyDao
import com.peter.thelandlord.domain.interfaces.PropertyManagementRepo
import com.peter.thelandlord.domain.models.Property
import com.peter.thelandlord.utils.Constants
import com.peter.thelandlord.utils.FirestoreCollections
import com.peter.thelandlord.utils.Utils.getCurrentTimestamp
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
        property.timestamp = getCurrentTimestamp().also {
            Log.d(TAG, it)
        }

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

    fun getAllPropertiesByEmail(email: String): DataSource.Factory<Int, Property>{
        return propertyDao.getOwnedProperties(email)
    }

    fun getPropertyByID(propertyID: String):LiveData<Property> = propertyDao.getPropertyById(propertyID)

}