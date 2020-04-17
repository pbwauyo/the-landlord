package com.peter.thelandlord.presentation.addproperty

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.peter.thelandlord.data.PropertyManagementRepoImpl
import com.peter.thelandlord.domain.models.Property
import com.peter.thelandlord.domain.usecases.propertyusecases.SavePropertyUseCase
import javax.inject.Inject

class PropertyViewModel (propertyManagementRepoImpl: PropertyManagementRepoImpl ): ViewModel() {

    companion object{
        const val EMPTY_FIELD_ERROR = "field cannot be empty"
        const val TAG = "PropertyViewModel"
    }

    val propertyNameLiveData = MutableLiveData<String>()
    val propertyLocationLiveData = MutableLiveData<String>()
    val errorLiveData = MutableLiveData<String>()
    val successLiveData = MutableLiveData<String>()

    val propertyNameErrorLiveData = MutableLiveData<String>()
    val propertyLocationErrorLiveData = MutableLiveData<String>()

    val isSavingLiveData = MutableLiveData<Boolean>()

    private val saveProperty = SavePropertyUseCase(propertyManagementRepoImpl)

    fun savePropertyDetails(){
        val propertyName = propertyNameLiveData.value?.trim()
        val propertyLocation = propertyLocationLiveData.value?.trim()
        var isPropertyNameEmpty = true
        var isPropertyLocationEmpty = true

        if (propertyName.isNullOrEmpty()) {
            propertyNameErrorLiveData.value = EMPTY_FIELD_ERROR
        }
        else {
            isPropertyNameEmpty = false
        }

        if (propertyLocation.isNullOrEmpty()) {
            propertyLocationErrorLiveData.value = EMPTY_FIELD_ERROR
        }
        else {
            isPropertyLocationEmpty = false
        }


        if(!(isPropertyNameEmpty || isPropertyLocationEmpty)) {
            isSavingLiveData.value = true
            val property = Property(propertyName!!, propertyLocation!!)
            saveProperty (property, isSavingLiveData, errorLiveData, successLiveData)
        }else{
            Log.d(TAG, "False")
        }

    }
}