package com.peter.thelandlord.data.dao

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.peter.thelandlord.domain.models.Payment
import io.reactivex.Completable

@Dao    //some transactions have observable variants
interface PaymentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun savePayment(payment: Payment)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun savePaymentCompletable(payment: Payment): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun savePaymentsCompletable(payments: List<Payment>): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun savePayments(payments: List<Payment>)

    @Query("SELECT * FROM payments WHERE payment_id = :paymentId LIMIT 1")
    fun getPaymentById(paymentId: String): Payment

    @Query("SELECT * FROM payments WHERE payment_id = :paymentId LIMIT 1")
    fun getPaymentByIdLD(paymentId: String): LiveData<Payment>

    @Query("SELECT * FROM payments WHERE rental_id = :rentalId")
    fun getAllPaymentsByRentalIdLD(rentalId: String): LiveData<List<Payment>>

    @Query("SELECT * FROM payments WHERE property_id = :propertyId")
    fun getAllPaymentsForPropertyLD(propertyId: String): DataSource.Factory<Int, Payment>

    @Query("SELECT * FROM payments WHERE timestamp = :timestamp")
    fun getAllPaymentsByTimestamp(timestamp: String): List<Payment>

}