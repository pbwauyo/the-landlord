package com.peter.thelandlord.domain.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "rentals")
data class Rental (
    @PrimaryKey var id: String = "",
    var rentalNumber: String = "",
    @ColumnInfo(name = "monthly_amount") var monthlyAmount: String = "",
    @ColumnInfo(name = "tenant_name") var tenantName: String = "",
    @ColumnInfo(name = "tenant_contact") var tenantContact: String = "",
    @ColumnInfo(name = "tenancy_start_date") var tenancyStartDate: String = "",
    @ColumnInfo(name = "rent_computation_start_date") var rentComputationStartDate: String = "",
    var propertyID: String = "",
    var timestamp: String = ""
){

    override fun equals(other: Any?): Boolean {
        return other is Rental &&
                this.id == other.id &&
                this.rentalNumber == other.rentalNumber &&
                this.monthlyAmount == other.monthlyAmount &&
                this.tenantName == other.tenantName &&
                this.tenantContact == other.tenantContact &&
                this.tenancyStartDate == other.tenancyStartDate &&
                this.propertyID == other.propertyID &&
                this.timestamp == other.timestamp
    }

    override fun hashCode(): Int {
        return timestamp.hashCode()
    }

}