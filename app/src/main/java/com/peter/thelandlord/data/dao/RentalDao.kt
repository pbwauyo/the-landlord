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

    @Delete
    fun deleteRental(rental: Rental): Completable

    @Query("SELECT * FROM rentals WHERE rentalNumber = :rentalId LIMIT 1")
    fun getRentalByIdLiveData(rentalId: String): LiveData<Rental>

    @Query("SELECT * FROM rentals WHERE rentalNumber = :rentalId")
    fun findRentalById(rentalId: String): Rental

    @Query("SELECT * FROM rentals WHERE propertyID = :propertyId")
    fun getRentalsByProperty(propertyId: String): DataSource.Factory<Int, Rental>
}