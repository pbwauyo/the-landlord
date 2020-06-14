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
    val timestamp: String = ""
){
    override fun equals(other: Any?): Boolean {
        return other is Payment &&
                other.paymentId == this.paymentId &&
                other.dateOfPayment == this.dateOfPayment &&
                other.amount == this.amount
    }

    override fun hashCode(): Int {
        return timestamp.hashCode()
    }
}