package com.peter.thelandlord.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.peter.thelandlord.domain.models.RentalAccountSummary

@Dao
interface RentalAccountSummaryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveAccountSummary(rentalAccountSummary: RentalAccountSummary)

    @Query("SELECT * FROM rental_account_summary WHERE rental_id = :rentalId")
    fun getAccountSummary(rentalId: String): LiveData<RentalAccountSummary>

    @Query("UPDATE rental_account_summary SET outstanding_balance = :amount WHERE rental_id = :rentalId")
    fun updateOutstandingBalance(rentalId: String, amount: String)

    @Query("UPDATE rental_account_summary SET date_of_last_payment = :date WHERE rental_id = :rentalId")
    fun updateDateOfLastPayment(rentalId: String, date: String)

}