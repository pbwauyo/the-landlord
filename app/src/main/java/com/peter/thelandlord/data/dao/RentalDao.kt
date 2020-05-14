package com.peter.thelandlord.data.dao

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import com.peter.thelandlord.domain.models.Rental
import io.reactivex.Completable


@Dao
interface RentalDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveRental(rental: Rental): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveRentals(rentals: List<Rental>): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRental(rental: Rental)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRentals(rentals: List<Rental>)

    @Query("DELETE FROM rentals WHERE propertyID = :propertyId")
    fun deleteRentalByPropertyId(propertyId: String)

    @Delete
    fun deleteRental(rental: Rental): Completable

    @Query("SELECT * FROM rentals WHERE id = :rentalId LIMIT 1")
    fun getRentalByIdLiveData(rentalId: String): LiveData<Rental>

    @Query("SELECT * FROM rentals WHERE id = :rentalId")
    fun findRentalById(rentalId: String): Rental

    @Query("SELECT * FROM rentals WHERE propertyID = :propertyId")
    fun getRentalsByProperty(propertyId: String): DataSource.Factory<Int, Rental>
}