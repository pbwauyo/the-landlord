package com.peter.thelandlord.domain.interfaces

import androidx.lifecycle.MutableLiveData
import com.peter.thelandlord.domain.models.Rental

interface RentalManagementRepo {

    fun saveRental(rental: Rental, isSavingLiveData: MutableLiveData<Boolean>,
                     errorLiveData: MutableLiveData<String>, successLiveData: MutableLiveData<String>
    )

}