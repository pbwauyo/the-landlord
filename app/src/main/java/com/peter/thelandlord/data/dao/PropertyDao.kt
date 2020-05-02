package com.peter.thelandlord.data.dao

import androidx.paging.DataSource
import androidx.room.*
import com.peter.thelandlord.domain.models.Property
import io.reactivex.Completable
import io.reactivex.Observable

@Dao
interface PropertyDao {

    @Query("SELECT * FROM properties WHERE owner = :ownerId")
    fun getOwnedProperties(ownerId: String): DataSource.Factory<Int, Property>

    @Query("SELECT * FROM properties WHERE propertyID = :propertyId LIMIT 1")
    fun getPropertyById(propertyId: String): Observable<Property>

    @Query("SELECT * FROM properties WHERE propertyID = :propertyId LIMIT 1")
    fun findProperty(propertyId: String): Property

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveProperty(property: Property): Completable

    @Update
    fun updateProperty(property: Property): Completable

    @Delete
    fun deleteProperty(property: Property): Completable


}