package com.peter.thelandlord.domain.usecases.rentalusecases

import androidx.lifecycle.MutableLiveData
import com.peter.thelandlord.domain.interfaces.RentalManagementRepo
import com.peter.thelandlord.domain.models.Rental

class SaveRentalUseCase(private val rentalManagementRepo: RentalManagementRepo) {

    operator fun invoke(
        rental: Rental,
        isSavingLiveData: MutableLiveData<Boolean>,
        errorLiveData: MutableLiveData<String>,
        successLiveData: MutableLiveData<String>
    ){
      rentalManagementRepo.saveRental(rental, isSavingLiveData, errorLiveData, successLiveData)
    }

}