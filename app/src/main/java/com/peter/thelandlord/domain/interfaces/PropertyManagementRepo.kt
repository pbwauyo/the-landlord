package com.peter.thelandlord.domain.interfaces

import androidx.lifecycle.MutableLiveData
import com.peter.thelandlord.domain.models.Property

interface PropertyManagementRepo {

    fun saveProperty(property: Property, isSavingLiveData: MutableLiveData<Boolean>,
                     errorLiveData: MutableLiveData<String>, successLiveData: MutableLiveData<String>)

}