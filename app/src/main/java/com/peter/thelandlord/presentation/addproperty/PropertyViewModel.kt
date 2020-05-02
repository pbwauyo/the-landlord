package com.peter.thelandlord.presentation.addproperty

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peter.thelandlord.data.PropertyManagementRepoImpl
import com.peter.thelandlord.domain.models.Property
import com.peter.thelandlord.domain.usecases.propertyusecases.SavePropertyUseCase
import io.reactivex.Completable
import kotlinx.coroutines.launch

class PropertyViewModel (private val propertyManagementRepoImpl: PropertyManagementRepoImpl ): ViewModel() {

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

   // private val saveProperty = SavePropertyUseCase(propertyManagementRepoImpl)

    fun setSavingStatus(value: Boolean){
        isSavingLiveData.postValue(value)
    }

    fun setError(value: String){
        errorLiveData.postValue(value)
    }

    fun setSuccess(value: String){
        successLiveData.postValue(value)
    }

    fun savePropertyDetails(): Completable {

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

        return if(!(isPropertyNameEmpty || isPropertyLocationEmpty)) {
            isSavingLiveData.postValue(true)
            val property = Property(propertyName!!, propertyLocation!!)
            //saveProperty (property, isSavingLiveData, errorLiveData, successLiveData)

            propertyManagementRepoImpl.saveProperty(property)

        }else {
            Completable.error(Throwable("Please fill all fields"))
        }

    }

    fun clearFields(){
        propertyNameLiveData.postValue("")
        propertyLocationLiveData.postValue("")
    }

}