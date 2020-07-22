package com.peter.thelandlord.domain.interfaces

import androidx.lifecycle.LiveData
import com.peter.thelandlord.domain.models.Landlord

interface ProfileRepo {

    fun getLandlordProfile(email: String): LiveData<Landlord>

    fun updateLandlordProfile(landlord: Landlord)

    fun updateLandlordImage(imageUri: String)
}