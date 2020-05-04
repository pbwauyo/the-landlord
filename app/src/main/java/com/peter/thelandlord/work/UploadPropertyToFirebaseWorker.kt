package com.peter.thelandlord.work

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.firebase.firestore.FirebaseFirestore
import com.peter.thelandlord.data.db.AppDatabase
import com.peter.thelandlord.domain.models.Property
import com.peter.thelandlord.utils.Constants
import com.peter.thelandlord.utils.FirestoreCollections
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.tasks.await

class UploadPropertyToFirebaseWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    private companion object{
        const val TAG = "UPLOAD_PROPERTY_WORKER"
    }

    override suspend fun doWork(): Result = coroutineScope {
        val firestore = FirebaseFirestore.getInstance()
        val propertyID = inputData.getString(Constants.KEY_PROPERTY_ID)
        val appDatabase = AppDatabase.getInstance(applicationContext)

        val property: Property = appDatabase.propertyDao().findProperty(propertyID!!)

        try {
            firestore.collection(FirestoreCollections.PROPERTIES)
                .document(propertyID)
                .set(property).await()
            Result.success()
        }catch (e: Exception){
            Log.d(TAG, "$e")
            Result.retry()
        }

    }
}