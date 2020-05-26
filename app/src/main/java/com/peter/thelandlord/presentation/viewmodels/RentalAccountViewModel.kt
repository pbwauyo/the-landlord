package com.peter.thelandlord.presentation.viewmodels

import android.widget.EditText
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.peter.thelandlord.domain.interfaces.RentalAccountRepo
import com.peter.thelandlord.domain.models.Payment
import io.reactivex.Completable

class RentalAccountViewModel(val rentalAccountRepo: RentalAccountRepo) : ViewModel() {

    val debtsMapList: ArrayList<Map<String, EditText>> = ArrayList()

    val propertyIdLiveData = MutableLiveData<String>()
    val propertyDebtsLiveData = propertyIdLiveData.switchMap {
        rentalAccountRepo.getAllDebtsForProperty(it)
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
        propertyIdLiveData.value = value
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

}