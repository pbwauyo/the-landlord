package layout

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.peter.thelandlord.data.apis.RentalsApi
import com.peter.thelandlord.utils.Constants
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.tasks.await
import java.lang.Exception

class RemoveTenantDetailsWorker(context: Context, params: WorkerParameters): CoroutineWorker(context, params) {

    companion object{
        const val TAG = "REMOVE_TENANT"
    }

    override suspend fun doWork(): Result = coroutineScope {

        try {
            val rentalId: String = inputData.getString(Constants.KEY_RENTAL_ID)!!
            RentalsApi.removeTenantDetails(rentalId).await()
            Result.success()
        }catch (e: Exception){
            Log.d(TAG, "${e.message}")
            Result.retry()
        }
    }
}