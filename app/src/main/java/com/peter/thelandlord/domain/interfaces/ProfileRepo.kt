package com.peter.thelandlord.domain.interfaces

import com.peter.thelandlord.domain.models.Landlord

interface ProfileRepo {

    fun getLandlordProfile(email: String): Landlord

    fun updateLandlordProfile(landlord: Landlord)

    fun updateLandlordImage(imageUri: String)
}