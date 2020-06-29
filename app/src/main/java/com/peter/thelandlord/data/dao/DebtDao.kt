package com.peter.thelandlord.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.peter.thelandlord.domain.models.Debt

@Dao
interface DebtDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveDebts(debts: List<Debt>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveDebt(debt: Debt)

    @Query("DELETE FROM debts WHERE rental_id = :rentalId")
    fun deleteDebtsForRental(rentalId: String)

    @Query("DELETE FROM debts WHERE property_id = :propertyId")
    fun deleteDebtsForProperty(propertyId: String)

    @Query("SELECT * FROM debts WHERE property_id = :propertyId ORDER BY year, month")
    fun getPropertyDebtsLD(propertyId: String): LiveData<List<Debt>>

    @Query("SELECT * FROM debts WHERE rental_id = :rentalId ORDER BY year, month")
    fun getRentalDebtsLD(rentalId: String): LiveData<List<Debt>>

    @Query("SELECT * FROM debts WHERE property_id = :propertyId")
    fun getPropertyDebts(propertyId: String): List<Debt>

    @Query("SELECT * FROM debts WHERE rental_id = :rentalId")
    fun getRentalDebts(rentalId: String): List<Debt>
}