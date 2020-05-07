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
    var propertyID: String = "",
    var timestamp: String = ""
)