package com.peter.thelandlord.data

import androidx.lifecycle.LiveData
import com.peter.thelandlord.data.db.AppDatabase
import com.peter.thelandlord.domain.interfaces.ProfileRepo
import com.peter.thelandlord.domain.models.Landlord
import javax.inject.Inject

class ProfileRepoImpl @Inject constructor ( appDb: AppDatabase) : ProfileRepo {

    val landlordDao = appDb.landlordDao()

    override fun getLandlordProfile(email: String): LiveData<Landlord> {
        return landlordDao.getLandlordByEmail(email)
    }

    override fun updateLandlordProfile(landlord: Landlord) {
        TODO("Not yet implemented")
    }

    override fun updateLandlordImage(imageUri: String) {
        TODO("Not yet implemented")
    }

}