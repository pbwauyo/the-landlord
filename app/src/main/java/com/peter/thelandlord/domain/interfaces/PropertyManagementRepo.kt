package com.peter.thelandlord.domain.interfaces

import com.peter.thelandlord.domain.models.Property
import io.reactivex.Completable

interface PropertyManagementRepo {

//    fun saveProperty(property: Property, isSavingLiveData: MutableLiveData<Boolean>,
//                     errorLiveData: MutableLiveData<String>, successLiveData: MutableLiveData<String>)

    fun saveProperty(property: Property): Completable

}