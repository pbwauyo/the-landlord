package com.peter.thelandlord.domain.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "landlords")
data class Landlord(
    @PrimaryKey var email: String="",
    @ColumnInfo(name = "first_name") var firstName: String="",
    @ColumnInfo(name = "last_name") var lastName: String="",
    @ColumnInfo(name="image_url") var imageUrl: String="",
    var password: String="",
    var timestamp: String = ""
)