package com.peter.thelandlord.domain.usecases.authusecases

import androidx.lifecycle.MutableLiveData
import com.peter.thelandlord.domain.interfaces.AuthRepositoryInterface
import com.peter.thelandlord.domain.models.Landlord
import com.peter.thelandlord.singleliveevent.SingleLiveEvent

class RegisterUseCase constructor(private val authRepositoryInterface: AuthRepositoryInterface) {

    suspend operator fun invoke(landlord: Landlord,
                                errorLiveData: MutableLiveData<String>,
                                emailLiveData: MutableLiveData<String>,
                                isRegisteringLiveData: MutableLiveData<Boolean>,
                                isSignedInLiveData: SingleLiveEvent<Boolean>,
                                successLiveData: MutableLiveData<String>
    ){
        authRepositoryInterface.createUser(landlord, errorLiveData, emailLiveData,
            isRegisteringLiveData, isSignedInLiveData, successLiveData)
    }

}