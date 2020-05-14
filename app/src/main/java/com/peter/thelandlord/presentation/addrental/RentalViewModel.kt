package com.peter.thelandlord.presentation.addrental

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import com.peter.thelandlord.data.RentalManagementRepoImpl
import com.peter.thelandlord.domain.models.Rental
import io.reactivex.Completable

class RentalViewModel(val rentalManagementRepoImpl: RentalManagementRepoImpl): ViewModel() {

    private companion object {
        const val EMPTY_FIELD_ERROR = "field cannot be empty"
        const val TAG = "RENTAL_VIEW_MODEL"
    }

    val currentRentalIdLiveData = MutableLiveData<String>()
    val currentRentalLiveData = currentRentalIdLiveData.switchMap {
        Log.d(TAG, it)
        rentalManagementRepoImpl.getRentalLiveData(it)
    }
    val currentPropertyIdForRentalLiveData = MutableLiveData<String>()
    val currentPropertyDetailsForRental = currentPropertyIdForRentalLiveData.switchMap {
        rentalManagementRepoImpl.getPropertyDetailsForRental(it)
    }

    val propertyIDLiveData = MutableLiveData<String>()
    private val repoResult = propertyIDLiveData.map {
        rentalManagementRepoImpl.rentalsFromProperty(it)
    }

    val networkState = repoResult.switchMap {
        it.networkState
    }

    val refreshState = repoResult.switchMap {
        it.refreshState
    }

    val rentalsPagedList = repoResult.switchMap {
        it.livePagedList
    }


    //input fields
    val rentalNumberLiveData = MutableLiveData<String>()
    val monthlyAmountLiveData = MutableLiveData<String>()
    val tenantNameLiveData = MutableLiveData<String>()
    val tenantContactLiveData = MutableLiveData<String>()
    val tenancyStartDateLiveData = MutableLiveData<String>()
    val rentalComputationStartMonthLiveData = MutableLiveData<String>()
    val rentalComputationStartYearLiveData = MutableLiveData<String>()

    val errorLiveData = MutableLiveData<String>()
    val successLiveData = MutableLiveData<String>()
    val isSavingLiveData = MutableLiveData<Boolean>()

    //error fields
    val rentalNumberErrorLiveData = MutableLiveData<String>()
    val monthlyAmountErrorLiveData = MutableLiveData<String>()
    val tenantNameErrorLiveData = MutableLiveData<String>()
    val tenantContactErrorLiveData = MutableLiveData<String>()
    val tenancyStartDateErrorLiveData = MutableLiveData<String>()
    val tenantDetailsErrorLiveData = MutableLiveData<String>()
    val rentComputStartMonthErrorLiveData = MutableLiveData<String>()
    val rentComputStartYearErrorLiveData = MutableLiveData<String>()


    private fun allFieldsAreValid(): Boolean{

        val rentalID = rentalNumberLiveData.value?.trim()
        val monthlyAmount = monthlyAmountLiveData.value?.trim()

        var isRentalIDEmpty = true
        var isMonthlyAmountEmpty = true

        if (rentalID.isNullOrBlank()){
            rentalNumberErrorLiveData.postValue(EMPTY_FIELD_ERROR)
        }
        else{
            isRentalIDEmpty = false
        }

        if(monthlyAmount.isNullOrBlank()){
            monthlyAmountErrorLiveData.postValue(EMPTY_FIELD_ERROR)
        }
        else{
            isMonthlyAmountEmpty = false
        }



        return !isRentalIDEmpty && !isMonthlyAmountEmpty && tenantDetailsAreValid()
    }

    private fun tenantDetailsAreValid(): Boolean{

        val tenantName = tenantNameLiveData.value?.trim()
        val tenantContact = tenantContactLiveData.value?.trim()
        val tenancyStartDate = tenancyStartDateLiveData.value?.trim()
        val rentComputationMonth = rentalComputationStartMonthLiveData.value?.trim()
        val rentComputationYear = rentalComputationStartYearLiveData.value?.trim()

        var areTenantDetailsValid = true
        var isTenantContactOk = true
        var isTenancyStartDateOk = true
        var isRentComptMonthOk = true
        var isRentComptYearOk = true

        if (!tenantName.isNullOrBlank()){

            if (tenantContact.isNullOrBlank()){
                isTenantContactOk = false
                tenantContactErrorLiveData.value = EMPTY_FIELD_ERROR
            }

            if(tenancyStartDate.isNullOrBlank()){
                isTenancyStartDateOk = false
                tenancyStartDateErrorLiveData.value = EMPTY_FIELD_ERROR
            }

            if (rentComputationMonth.isNullOrBlank()){
                isRentComptMonthOk = false
                rentComputStartMonthErrorLiveData.value = EMPTY_FIELD_ERROR
            }

            if(rentComputationYear.isNullOrBlank()){
                isRentComptYearOk = false
                rentComputStartYearErrorLiveData.value = EMPTY_FIELD_ERROR
            }

            if(!isTenantContactOk && !isTenancyStartDateOk && !isRentComptMonthOk && !isRentComptYearOk){
                areTenantDetailsValid = false
            }
        }

        Log.d(TAG, "Tenant details valid, $areTenantDetailsValid")

        return areTenantDetailsValid
    }


    fun saveRental(propertyID: String): Completable{

        val rentalID = rentalNumberLiveData.value?.trim()
        val monthlyAmount = monthlyAmountLiveData.value?.trim()
        val tenantName = tenantNameLiveData.value?.trim() ?: ""
        val tenantContact = tenantContactLiveData.value?.trim() ?: ""
        val tenancyStartDate = tenancyStartDateLiveData.value?.trim() ?: ""
        val rentComputationMonth = rentalComputationStartMonthLiveData.value?.trim()
        val rentComputationYear = rentalComputationStartYearLiveData.value?.trim()
        val rentComputationStartDate = if (tenantName != "")
            "${rentComputationMonth}/${rentComputationYear}"
            else ""

        return if (allFieldsAreValid()){
            setSavingStatus(true)

            val rental = Rental(rentalNumber = rentalID!!,
                monthlyAmount = monthlyAmount!!,
                tenantName = tenantName,
                tenantContact = tenantContact,
                tenancyStartDate = tenancyStartDate,
                propertyID = propertyID,
                rentComputationStartDate = rentComputationStartDate)

            rentalManagementRepoImpl.saveRental(rental)
        }else{
            Completable.error(Throwable("Please fill in all fields correctly"))
        }

    }

    fun clearFields(){
        rentalNumberLiveData.value = ""
        monthlyAmountLiveData.value = ""
        tenantNameLiveData.value = ""
        tenantContactLiveData.value = ""
        tenancyStartDateLiveData.value = ""
        rentalComputationStartMonthLiveData.value = ""
        rentalComputationStartYearLiveData.value = ""
    }

    fun setPropertyId(propertyID: String){
        propertyIDLiveData.value = propertyID
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

    fun refresh(){
        repoResult.value?.refresh?.invoke()
    }

    fun setRentStartMonth(value: String){
        rentalComputationStartMonthLiveData.value = value
    }

    fun setRentStartYear(value: String){
        rentalComputationStartYearLiveData.value = value
    }

    fun setTenacyStartDate(value: String){
        tenancyStartDateLiveData.value = value
    }

    fun setCurrentRentalId(value: String){
        currentRentalIdLiveData.value = value
    }

    fun setCurrentPropertyIdForRental(propertyID: String){
        currentPropertyIdForRentalLiveData.value = propertyID
    }

    fun retry(){
        repoResult.value?.retry?.invoke()
    }

    fun removeTenantDetails(): Completable{
        val rental = currentRentalLiveData.value
        rental?.tenantName = ""
        rental?.tenantContact = ""
        rental?.tenancyStartDate = ""

        return rentalManagementRepoImpl.removeTenantDetails(rental!!)
    }

    fun updateTenantDetails(): Completable{
        TODO()
    }
}