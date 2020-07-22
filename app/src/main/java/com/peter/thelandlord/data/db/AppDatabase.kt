package com.peter.thelandlord.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.peter.thelandlord.data.dao.*
import com.peter.thelandlord.domain.models.*

@Database(entities = [
    Property::class,
    Landlord::class,
    Rental::class,
    Payment::class,
    Debt::class,
    RentalAccountSummary::class], version = 9, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
    abstract fun propertyDao(): PropertyDao
    abstract fun landlordDao(): LandlordDao
    abstract fun rentalDao(): RentalDao
    abstract fun paymentDao(): PaymentDao
    abstract fun debtDao(): DebtDao
    abstract fun rentalAccountSummaryDao(): RentalAccountSummaryDao

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