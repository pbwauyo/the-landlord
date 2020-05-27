package com.peter.thelandlord.work

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.peter.thelandlord.data.apis.RentalAccountApi
import com.peter.thelandlord.data.db.AppDatabase
import com.peter.thelandlord.utils.Constants
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.tasks.await

class UploadPaymentsWorker(contex:Context, params: WorkerParameters) : CoroutineWorker(contex, params) {

    companion object{
        const val TAG = "UPLOAD_PAYMENTS"
    }

    override suspend fun doWork(): Result = coroutineScope {

        try {
            val appDatabase = AppDatabase.getInstance(applicationContext)
            val paymentDao = appDatabase.paymentDao()
            val timestamp = inputData.getString(Constants.KEY_TIMESTAMP)
            Log.d(TAG, "Timestamp, $timestamp")

            val payments = paymentDao.getAllPaymentsByTimestamp(timestamp!!)

            Log.d(TAG, "PAYMENTS, $payments")

            RentalAccountApi.uploadPayments(payments).await()
            Result.Retry.success()
        }catch (e: Exception){
            Log.d(TAG, "ERR, ${e.message}")

            Result.retry()
        }
    }

}