package com.peter.thelandlord.domain.usecases.authusecases

import androidx.lifecycle.MutableLiveData
import com.peter.thelandlord.domain.interfaces.AuthRepositoryInterface
import com.peter.thelandlord.domain.models.Landlord
import com.peter.thelandlord.domain.models.Login
import com.peter.thelandlord.singleliveevent.SingleLiveEvent

class LoginUseCase constructor(private val authRepositoryInterface: AuthRepositoryInterface) {

    operator fun invoke(
        login: Login,
        emailLiveData: MutableLiveData<String>,
        errorLiveData: MutableLiveData<String>,
        loggingInLiveData: MutableLiveData<Boolean>,
        isSignedInLiveData: SingleLiveEvent<Boolean>
    ) = authRepositoryInterface.loginUser(login, emailLiveData, errorLiveData, loggingInLiveData, isSignedInLiveData)

}

