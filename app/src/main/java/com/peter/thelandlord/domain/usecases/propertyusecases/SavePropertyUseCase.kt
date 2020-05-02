package com.peter.thelandlord.domain.usecases.propertyusecases

import androidx.lifecycle.MutableLiveData
import com.peter.thelandlord.domain.interfaces.PropertyManagementRepo
import com.peter.thelandlord.domain.models.Property

class SavePropertyUseCase (private val propertyManagementRepository: PropertyManagementRepo) {

//    operator fun invoke(property: Property, isSavingLiveData: MutableLiveData<Boolean>,
//                        errorLiveData: MutableLiveData<String>, successLiveData: MutableLiveData<String>
//    ){
//      propertyManagementRepository.saveProperty(property,
//          isSavingLiveData, errorLiveData, successLiveData)
//    }


    operator fun invoke (){

    }
}