package com.peter.thelandlord.presentation.addrental

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.peter.thelandlord.data.RentalManagementRepoImpl
import com.peter.thelandlord.domain.models.Rental
import io.reactivex.Completable

class RentalViewModel( val rentalManagementRepoImpl: RentalManagementRepoImpl): ViewModel() {

    private companion object {
        const val EMPTY_FIELD_ERROR = "field cannot be empty"
    }

    val rentalNumberLiveData = MutableLiveData<String>()
    val monthlyAmountLiveData = MutableLiveData<String>()
    val tenantNameLiveData = MutableLiveData<String>()
    val tenantContactLiveData = MutableLiveData<String>()
    val tenancyStartDateLiveData = MutableLiveData<String>()

    val errorLiveData = MutableLiveData<String>()
    val successLiveData = MutableLiveData<String>()
    val isSavingLiveData = MutableLiveData<Boolean>()

    val rentalNumberErrorLiveData = MutableLiveData<String>()
    val monthlyAmountErrorLiveData = MutableLiveData<String>()
    val tenantNameErrorLiveData = MutableLiveData<String>()
    val tenantContactErrorLiveData = MutableLiveData<String>()
    val tenancyStartDateErrorLiveData = MutableLiveData<String>()

    private fun allFieldsAreValid(): Boolean{

        val rentalID = rentalNumberLiveData.value?.trim()
        val monthlyAmount = monthlyAmountLiveData.value?.trim()
        val tenantName = tenantNameLiveData.value?.trim()
        val tenantContact = tenantContactLiveData.value?.trim()
        val tenancyStartDate = tenancyStartDateLiveData.value?.trim()

        var isRentalIDEmpty = true
        var isMonthlyAmountEmpty = true
        var isTenantNameEmpty = true
        var isTenantContactEmpty = true
        var isTenancyStartDateEmpty = true

        if (rentalID.isNullOrEmpty()){
            rentalNumberErrorLiveData.postValue(EMPTY_FIELD_ERROR)
        }
        else{
            isRentalIDEmpty = false
        }

        if(monthlyAmount.isNullOrEmpty()){
            monthlyAmountErrorLiveData.postValue(EMPTY_FIELD_ERROR)
        }
        else{
            isMonthlyAmountEmpty = false
        }

        if (tenantName.isNullOrEmpty()){
            tenantNameErrorLiveData.postValue(EMPTY_FIELD_ERROR)
        }
        else{
            isTenantNameEmpty = false
        }

        if (tenantContact.isNullOrEmpty()){
            tenantContactErrorLiveData.postValue(EMPTY_FIELD_ERROR)
        }
        else{
            isTenantContactEmpty = false
        }

        if (tenancyStartDate.isNullOrEmpty()){
            tenancyStartDateErrorLiveData.postValue(EMPTY_FIELD_ERROR)
        }
        else{
            isTenancyStartDateEmpty = false
        }

        return !(isRentalIDEmpty || isMonthlyAmountEmpty || isTenantNameEmpty || isTenantContactEmpty || isTenancyStartDateEmpty)
    }


    fun saveRental(propertyID: String): Completable{
        val rentalID = rentalNumberLiveData.value?.trim()
        val monthlyAmount = monthlyAmountLiveData.value?.trim()
        val tenantName = tenantNameLiveData.value?.trim()
        val tenantContact = tenantContactLiveData.value?.trim()
        val tenancyStartDate = tenancyStartDateLiveData.value?.trim()

        return if (allFieldsAreValid()){
            setSavingStatus(true)

            val rental = Rental(rentalNumber = rentalID!!,
                monthlyAmount = monthlyAmount!!,
                tenantName = tenantName!!,
                tenantContact = tenantContact!!,
                tenancyStartDate = tenancyStartDate!!,
                propertyID = propertyID)

            rentalManagementRepoImpl.saveRental(rental)
        }else{
            Completable.error(Throwable("Please fill in all fields correctly"))
        }

    }

    fun clearFields(){
        rentalNumberLiveData.postValue("")
        monthlyAmountLiveData.postValue("")
        tenantNameLiveData.postValue("")
        tenantContactLiveData.postValue("")
        tenancyStartDateLiveData.postValue("")
    }

    fun setSavingStatus(value: Boolean){
        isSavingLiveData.postValue(value)
    }

    fun setErrorValue(value: String){
        errorLiveData.postValue(value)
    }

    fun setSuccessValue(value: String){
        successLiveData.postValue(value)
    }
}