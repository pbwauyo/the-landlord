package com.peter.thelandlord.data.dao

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import com.peter.thelandlord.domain.models.Property
import io.reactivex.Completable

@Dao
interface PropertyDao {

    @Query("SELECT * FROM properties WHERE owner = :ownerEmail")
    fun getOwnedProperties(ownerEmail: String): DataSource.Factory<Int, Property>

    @Query("SELECT * FROM properties WHERE propertyID = :propertyId LIMIT 1")
    fun getPropertyById(propertyId: String): LiveData<Property>

    @Query("SELECT * FROM properties WHERE propertyID = :propertyId LIMIT 1")
    fun findProperty(propertyId: String): Property

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveProperty(property: Property): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProperty(property: Property)

    @Update
    fun updateProperty(property: Property): Completable

    @Delete
    fun deleteProperty(property: Property): Completable


}