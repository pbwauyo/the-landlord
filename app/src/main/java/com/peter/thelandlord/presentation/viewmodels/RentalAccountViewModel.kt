package com.peter.thelandlord.presentation.viewmodels

import android.util.Log
import android.widget.EditText
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import com.peter.thelandlord.domain.interfaces.RentalAccountRepo
import com.peter.thelandlord.domain.models.Payment
import io.reactivex.Completable

class RentalAccountViewModel(val rentalAccountRepo: RentalAccountRepo) : ViewModel() {

    companion object {
        const val TAG = "RENTAL_ACC_VM"
    }

    val debtsSearchTextLiveData = MutableLiveData<String>()
    val paymentsSearchTextLiveData = MutableLiveData<String>()

    val debtsMapList: ArrayList<Map<String, EditText>> = ArrayList()

    val propertyIdLiveData = MutableLiveData<String>()
    val propertyDebtsLiveData = propertyIdLiveData.switchMap {
        rentalAccountRepo.getAllDebtsForProperty(it)
    }

    val searchTextLiveData = MutableLiveData<String>()

    // handle payments fetch

    val repoResult = propertyIdLiveData.map {
        rentalAccountRepo.handlePaymentsFetching(it)
    }

    val networkState = repoResult.switchMap {
        it.networkState
    }

    val refreshState = repoResult.switchMap {
        it.refreshState
    }

    val paymentsPagedList = repoResult.switchMap {
        it.livePagedList
    }

    val rentalIdLiveData = MutableLiveData<String>()
    val rentalDebtsLiveData = rentalIdLiveData.switchMap {
        rentalAccountRepo.getAllDebtsForRental(it)
    }

    val dateOfPaymentLiveData = MutableLiveData<String>()

    fun setDateOfPayment(value: String?){
        dateOfPaymentLiveData.value = value
    }

    fun getDateOfPayment(): String?{
        return dateOfPaymentLiveData.value
    }

    fun setPropertyId(value: String?){
        if(propertyIdLiveData.value != value) propertyIdLiveData.value = value
    }

    fun setRentalId(value: String?){
        rentalIdLiveData.value = value
    }

    fun savePayments(payments: List<Payment>): Completable{
        return rentalAccountRepo.savePayments(payments)
    }

    fun getRentalId(): String{
        return rentalIdLiveData.value!!
    }

    fun getPropertyId(): String{
        return propertyIdLiveData.value!!
    }

    fun refresh(){
        repoResult.value?.refresh?.invoke()
    }

    fun retry(){
        repoResult.value?.retry?.invoke()
    }

}