package com.peter.thelandlord.domain.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.Exclude

@Entity(tableName = "rental_account_summary")
data class RentalAccountSummary (
    @get:Exclude @PrimaryKey val id: String = "",
    @ColumnInfo(name = "rental_id") val rentalId: String = "",
    @ColumnInfo(name = "outstanding_balance") val outstandingBalance: String = "",
    @ColumnInfo(name = "date_of_last_payment") val dateOfLastPayment: String = ""
)