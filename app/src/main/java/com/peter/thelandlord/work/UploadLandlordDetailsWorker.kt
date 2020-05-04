package com.peter.thelandlord.work

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.peter.thelandlord.data.db.AppDatabase
import com.peter.thelandlord.utils.Constants
import com.peter.thelandlord.utils.FirestoreCollections

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.tasks.await

class UploadLandlordDetailsWorker(context: Context, workerParams: WorkerParameters): CoroutineWorker(context, workerParams) {

    private companion object{
        const val TAG = "UPLOAD_LANDLORD"
    }

    override suspend fun doWork(): Result = coroutineScope {

        val landlordDao = AppDatabase.getInstance(applicationContext).landlordDao()
        val firestore = Firebase.firestore
        val emailKey = inputData.getString(Constants.KEY_LANDLORD_EMAIL)
        val landlord = landlordDao.findLandlord(emailKey!!)

        Log.d(TAG, "$emailKey")
        Log.d(TAG, "$landlord")


        try {
            firestore.collection(FirestoreCollections.USERS).add(landlord).await()
            Result.success()
        }catch (e: Exception){
            Log.d(TAG, "${e.message}")
            Result.retry()
        }

    }
}