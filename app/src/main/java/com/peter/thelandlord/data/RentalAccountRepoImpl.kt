package com.peter.thelandlord.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.work.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import com.peter.thelandlord.data.apis.RentalAccountApi
import com.peter.thelandlord.data.db.AppDatabase
import com.peter.thelandlord.data.listing.Listing
import com.peter.thelandlord.domain.interfaces.RentalAccountRepo
import com.peter.thelandlord.domain.models.Debt
import com.peter.thelandlord.domain.models.Payment
import com.peter.thelandlord.utils.Constants
import com.peter.thelandlord.utils.FirestoreCollections
import com.peter.thelandlord.work.UploadPaymentsWorker
import io.reactivex.Completable
import java.util.concurrent.Executor
import javax.inject.Inject

class RentalAccountRepoImpl @Inject constructor (
    val appDatabase: AppDatabase,
    val workManager: WorkManager,
    val ioExecutor: Executor ) :
    RentalAccountRepo {

    private val firestore = Firebase.firestore
    companion object {
        const val TAG = "RENTAL_ACC_REPO"
    }

    private val paymentDao = appDatabase.paymentDao()
    private val debtDao = appDatabase.debtDao()

    override fun getAllDebtsForProperty(propertyId: String): LiveData<List<Debt>> {

        RentalAccountApi.getAllDebtsForProperty(propertyId)
            .addOnFailureListener {
                Log.d(TAG, "DEBTS FOR PROPERTY, ${it.message}")
            }
            .addOnSuccessListener {
                val debts: List<Debt> = it.toObjects()
                ioThread {
                    val currentDbDebts = debtDao.getRentalDebts(propertyId)
                    debts.forEach { debt ->
                        if(!currentDbDebts.contains(debt)){
                            debtDao.saveDebt(debt)
                        }
                    }

                }
            }

        return debtDao.getPropertyDebtsLD(propertyId)
    }

    override fun getAllDebtsForRental(rentalId: String): LiveData<List<Debt>> {

        RentalAccountApi.getAllDebtsForRental(rentalId)
            .addOnFailureListener {
                Log.d(TAG, "DEBTS FOR RENTAL, ${it.message}")
            }
            .addOnSuccessListener {
                val debts: List<Debt> = it.toObjects()
                ioThread {
                    val currentDbDebts = debtDao.getRentalDebts(rentalId)
                    debts.forEach { debt ->
                        if(!currentDbDebts.contains(debt)){
                            debtDao.saveDebt(debt)
                        }
                    }
                }
            }

        return debtDao.getRentalDebtsLD(rentalId)
    }

    override fun getAllPayments(propertyId: String): Listing<Payment> {
        TODO("Not yet implemented")
    }

    override fun getAllPaymentsByTimestamp(timestamp: String): List<Payment> {
        TODO("Not yet implemented")
    }

    override fun saveDebts(debts: List<Debt>){
        debtDao.saveDebts(debts)
    }

    override fun savePayments(payments: List<Payment>): Completable {

        val reference = firestore.collection(FirestoreCollections.PAYMENTS)
        payments.forEach {
            it.paymentId = reference.document().id
        }

        val data = workDataOf(
            Constants.KEY_TIMESTAMP to payments[0].timestamp
        )

        val constraints = Constraints.Builder()
             .setRequiredNetworkType(NetworkType.CONNECTED)
             .build()

        val uploadRequest = OneTimeWorkRequestBuilder<UploadPaymentsWorker>()
            .setInputData(data)
            .setConstraints(constraints)
            .build()

        workManager.enqueue(uploadRequest)

        return paymentDao.savePaymentsCompletable(payments)
    }

    private fun ioThread(f: () -> Unit){
        ioExecutor.execute(f)
    }
}
