package com.peter.thelandlord.work

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.peter.thelandlord.data.apis.RentalsApi
import com.peter.thelandlord.data.db.AppDatabase
import com.peter.thelandlord.domain.models.Rental
import com.peter.thelandlord.domain.models.Tenant
import com.peter.thelandlord.utils.Constants
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.tasks.await

class UpdateTenantDetailsWorker(context: Context, params: WorkerParameters): CoroutineWorker(context, params) {

    companion object {
        const val TAG = "UPDATE_TENANT"
    }

    override suspend fun doWork(): Result = coroutineScope {

        try {
            val appDb = AppDatabase.getInstance(applicationContext)
            val rentalDao = appDb.rentalDao()
            val rentalId = inputData.getString(Constants.KEY_RENTAL_ID)!!
            val rental: Rental = rentalDao.findRentalById(rentalId)
            val tenant = Tenant(
                name = rental.tenantName,
                contact = rental.tenantContact,
                startDate = rental.tenancyStartDate
            )

            RentalsApi.updateTenantDetails(tenant, rentalId).await()
            Result.success()
        }catch (e: Exception){
            Log.d(TAG, "${e.message}")
            Result.retry()
        }
    }

}