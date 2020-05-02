package com.peter.thelandlord.domain.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.peter.thelandlord.objects.TableNames


@Entity(tableName = TableNames.PROPERTIES)
data class Property(
    var name: String = "",
    var location: String = "",
    var owner: String = "",
    @PrimaryKey  var propertyID: String= "",
    @ColumnInfo(name="image_url") var imageUrl: String = ""
)