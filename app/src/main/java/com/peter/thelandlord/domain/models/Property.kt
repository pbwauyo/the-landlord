package com.peter.thelandlord.domain.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.peter.thelandlord.utils.TableNames


@Entity(tableName = TableNames.PROPERTIES)
data class Property(
    var name: String = "",
    var location: String = "",
    var owner: String = "",
    @PrimaryKey  var propertyID: String= "",
    @ColumnInfo(name="image_url") var imageUrl: String = "",
    var timestamp: String = ""
){
    override fun equals(other: Any?): Boolean {
        return  other is Property &&
                this.imageUrl == other.imageUrl &&
                this.name == other.name &&
                this.location == other.location &&
                this.propertyID == other.propertyID &&
                this.owner ==  other.owner
    }

    override fun hashCode(): Int {
        return timestamp.hashCode()
    }
}