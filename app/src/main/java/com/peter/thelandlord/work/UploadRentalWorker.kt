package com.peter.thelandlord.work

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import com.peter.thelandlord.data.db.AppDatabase
import com.peter.thelandlord.utils.Constants
import com.peter.thelandlord.utils.FirestoreCollections
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.tasks.await
import java.lang.Exception

class UploadRentalWorker(context: Context, workerParams: WorkerParameters):
    CoroutineWorker(context, workerParams) {

    companion object{
        const val TAG = "UPLOAD_RENTAL_WORKER"
        val functions: FirebaseFunctions = Firebase.functions
    }

    override suspend fun doWork(): Result = coroutineScope {

            Log.d(TAG, "CURRENT USER, ${FirebaseAuth.getInstance().currentUser?.email}")

            try {
                val rentalDao = AppDatabase.getInstance(applicationContext).rentalDao()
                val firestore = Firebase.firestore
                val rentalId = inputData.getString(Constants.KEY_RENTAL_ID)
                val rental = rentalDao.findRentalById(rentalId!!)

                Log.d(TAG, "RENTAL, $rental")

                firestore.collection(FirestoreCollections.RENTALS).document(rental!!.id).set(rental).await()

                Result.success()
            }catch (e: Exception){
                Log.d(TAG, "ERROR, ${e.message}")
                Result.retry()
            }

    }

//    private fun fireInitialOutstandingBalFunction(rentalId: String, amount: String): Task<Void> {
//
//        val data = hashMapOf(
//            "rentalId" to rentalId,
//            "amount" to amount
//        )
//
//        return functions.getHttpsCallable("calculateInitialOutstandingBalance")
//            .call(data)
//    }

}