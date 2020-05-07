package com.peter.thelandlord.data

import androidx.work.*
import com.peter.thelandlord.utils.Utils.getCurrentTimestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.peter.thelandlord.data.dao.RentalDao
import com.peter.thelandlord.domain.interfaces.RentalManagementRepo
import com.peter.thelandlord.domain.models.Rental
import com.peter.thelandlord.utils.Constants
import com.peter.thelandlord.utils.FirestoreCollections
import com.peter.thelandlord.work.UploadRentalWorker
import io.reactivex.Completable

class RentalManagementRepoImpl (val firestore: FirebaseFirestore, val rentalDao: RentalDao, val workManager: WorkManager) : RentalManagementRepo {
  //  private val executor = Executors.newSingleThreadExecutor()

    companion object{
        const val TAG = "RENTAL_MANGT_REPO"
    }

    override fun saveRental(rental: Rental ): Completable {

        rental.timestamp = getCurrentTimestamp()
        rental.id = firestore.collection(FirestoreCollections.RENTALS).document().id

        val data = workDataOf(Constants.KEY_RENTAL_ID to rental.rentalNumber)

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val workRequest = OneTimeWorkRequestBuilder<UploadRentalWorker>()
            .setInputData(data)
            .setConstraints(constraints)
            .build()

        workManager.enqueue(workRequest)

        return rentalDao.saveRental(rental)
    }

//    private fun ioExecutor(f: () -> Unit){
//        executor.execute(f)
//    }
}