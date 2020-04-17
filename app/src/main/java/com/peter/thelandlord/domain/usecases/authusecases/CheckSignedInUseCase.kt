package com.peter.thelandlord.domain.usecases.authusecases

import androidx.lifecycle.MutableLiveData
import com.peter.thelandlord.domain.interfaces.AuthRepositoryInterface
import com.peter.thelandlord.domain.models.Landlord
import com.peter.thelandlord.singleliveevent.SingleLiveEvent

class CheckSignedInUserUseCase constructor(private val authRepositoryInterface: AuthRepositoryInterface) {
    operator fun invoke(
        liveData: MutableLiveData<Landlord>,
        errorLiveData: MutableLiveData<String>,
        isSignedInLiveData: SingleLiveEvent<Boolean>
    ){
        authRepositoryInterface.isUserLoggedIn(liveData, errorLiveData, isSignedInLiveData)
    }
}