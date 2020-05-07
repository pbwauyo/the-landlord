package com.peter.thelandlord.presentation.addproperty

import androidx.lifecycle.*
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.peter.thelandlord.boundarycallbacks.PropertyBoundaryCallback
import com.peter.thelandlord.data.PropertyManagementRepoImpl
import com.peter.thelandlord.domain.models.Property
import io.reactivex.Completable

class PropertyViewModel (private val propertyManagementRepoImpl: PropertyManagementRepoImpl ): ViewModel() {

    companion object {
        const val EMPTY_FIELD_ERROR = "field cannot be empty"
        const val TAG = "PropertyViewModel"
        const val PAGE_SIZE = 15
    }

    val emailLiveData = MutableLiveData<String>()

    val config = PagedList.Config.Builder()
                .setPageSize(PAGE_SIZE)
                .build()

    val propertyListLiveData: LiveData<PagedList<Property>> = Transformations.switchMap(emailLiveData){

        val datasourceFactory = propertyManagementRepoImpl.getAllPropertiesByEmail(it)
        LivePagedListBuilder(datasourceFactory, config)
        .setBoundaryCallback(PropertyBoundaryCallback(it, propertyManagementRepoImpl.propertyDao, PAGE_SIZE))
        .build()
    }

    val propertyIDLLiveData = MutableLiveData<String>()
    val propertyLiveData = Transformations.switchMap(propertyIDLLiveData){
        propertyManagementRepoImpl.getPropertyByID(propertyIDLLiveData.value!!)
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
            val property = Property(name = propertyName!!, location = propertyLocation!!)
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

    fun setPropertyEmail(email: String){
        emailLiveData.postValue(email)
    }

    fun setPropertyID(propertyID: String){
        propertyIDLLiveData.postValue(propertyID)
    }

}