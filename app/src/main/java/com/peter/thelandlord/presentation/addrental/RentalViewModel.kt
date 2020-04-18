package com.peter.thelandlord.presentation.addrental

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.peter.thelandlord.data.RentalManagementRepoImpl
import com.peter.thelandlord.domain.models.Rental
import com.peter.thelandlord.domain.usecases.rentalusecases.SaveRentalUseCase

class RentalViewModel( rentalManagementRepoImpl: RentalManagementRepoImpl): ViewModel() {

    private companion object{
        const val EMPTY_FIELD_ERROR = "field cannot be empty"
    }

    val rentalIDLiveData = MutableLiveData<String>()
    val monthlyAmountLiveData = MutableLiveData<String>()
    val tenantNameLiveData = MutableLiveData<String>()
    val tenantContactLiveData = MutableLiveData<String>()
    val tenancyStartDateLiveData = MutableLiveData<String>()

    val errorLiveData = MutableLiveData<String>()
    val successLiveData = MutableLiveData<String>()
    val isSavingLiveData = MutableLiveData<Boolean>()

    val rentalIDErrorLiveData = MutableLiveData<String>()
    val monthlyAmountErrorLiveData = MutableLiveData<String>()
    val tenantNameErrorLiveData = MutableLiveData<String>()
    val tenantContactErrorLiveData = MutableLiveData<String>()
    val tenancyStartDateErrorLiveData = MutableLiveData<String>()

    val saveRental = SaveRentalUseCase(rentalManagementRepoImpl)

    fun saveRentalDetails(propertyID: String){

        val rentalID = rentalIDLiveData.value?.trim()
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
            rentalIDErrorLiveData.postValue(EMPTY_FIELD_ERROR)
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

        if (!(isRentalIDEmpty || isMonthlyAmountEmpty || isTenantNameEmpty || isTenantContactEmpty || isTenancyStartDateEmpty)){
            isSavingLiveData.postValue(true)

            val rental = Rental(rentalID!!, monthlyAmount!!, tenantName!!, tenantContact!!, tenancyStartDate!!, propertyID)
            saveRental(rental, isSavingLiveData, errorLiveData, successLiveData)
        }
    }

    fun clearFields(){
        rentalIDLiveData.postValue("")
        monthlyAmountLiveData.postValue("")
        tenantNameLiveData.postValue("")
        tenantContactLiveData.postValue("")
        tenancyStartDateLiveData.postValue("")
    }
}