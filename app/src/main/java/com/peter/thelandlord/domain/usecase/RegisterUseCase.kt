package com.peter.thelandlord.domain.usecase

import androidx.lifecycle.MutableLiveData
import com.peter.thelandlord.domain.interfaces.AuthRepositoryInterface
import com.peter.thelandlord.domain.models.Landlord
import com.peter.thelandlord.singleliveevent.SingleLiveEvent

class RegisterUseCase constructor(private val authRepositoryInterface: AuthRepositoryInterface) {

    operator fun invoke(landlord: Landlord,
                        liveData: MutableLiveData<Landlord>,
                        errorLiveData: MutableLiveData<String>,
                        isRegisteringLiveData: MutableLiveData<Boolean>,
                        isSignedInLiveData: SingleLiveEvent<Boolean>,
                        successLiveData: MutableLiveData<String>
    ){
        authRepositoryInterface.createUser(landlord, liveData, errorLiveData,
            isRegisteringLiveData, isSignedInLiveData, successLiveData)
    }

}