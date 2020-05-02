package com.peter.thelandlord.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.peter.thelandlord.domain.models.Landlord
import io.reactivex.Completable

@Dao
interface LandlordDao {

    @Query("SELECT * FROM landlords WHERE email = :email LIMIT 1")
    fun getLandlordByEmail(email: String): LiveData<Landlord>

    @Query("SELECT * FROM landlords WHERE email = :email LIMIT 1")
    fun findLandlord(email: String): Landlord

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveLandlord(landlord: Landlord)
}