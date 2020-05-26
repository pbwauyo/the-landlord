package com.peter.thelandlord.domain.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "debts")
data class Debt (
    @PrimaryKey val debtId: String = "",
    val amount: String = "",
    val month: String = "",
    val year: String = "",
    @ColumnInfo(name = "rental_id") val rentalId: String = "",
    @ColumnInfo(name = "property_id") val propertyId: String = ""
){
    override fun equals(other: Any?): Boolean {
        return  other is Debt &&
                other.amount == this.amount &&
                other.month == this.month &&
                other.year == this.year &&
                other.rentalId == this.rentalId
    }

    override fun hashCode(): Int {
        return debtId.hashCode()
    }
}