package com.peter.thelandlord.domain.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.Exclude

@Entity(tableName = "payments")
data class Payment (
    @PrimaryKey @ColumnInfo(name = "payment_id") var paymentId: String = "",
    val month: String = "",
    val year: String = "",
    val amount: String = "",
    @ColumnInfo(name = "rental_id") val rentalId: String = "",
    @ColumnInfo(name = "property_id") val propertyId: String = "",
    @ColumnInfo(name = "date_of_payment") val dateOfPayment: String = "",
    @get:Exclude val timestamp: String = "" // exclude from firestore storage
)