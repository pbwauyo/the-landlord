package com.peter.thelandlord.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.peter.thelandlord.data.dao.LandlordDao
import com.peter.thelandlord.data.dao.PropertyDao
import com.peter.thelandlord.data.dao.RentalDao
import com.peter.thelandlord.domain.models.Landlord
import com.peter.thelandlord.domain.models.Property
import com.peter.thelandlord.domain.models.Rental

@Database(entities = [Property::class, Landlord::class, Rental::class], version = 5)
abstract class AppDatabase: RoomDatabase() {
    abstract fun propertyDao(): PropertyDao
    abstract fun landlordDao(): LandlordDao
    abstract fun rentalDao(): RentalDao

    companion object {
        private const val DATABASE_NAME = "app-database.db"
        @Volatile private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase{
            return instance ?: synchronized(this){
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase{
            return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
                       .fallbackToDestructiveMigration()
                       .build()
        }
    }

}