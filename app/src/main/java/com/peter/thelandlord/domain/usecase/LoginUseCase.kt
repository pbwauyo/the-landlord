package com.peter.thelandlord.domain.usecase

import androidx.lifecycle.MutableLiveData
import com.peter.thelandlord.domain.interfaces.AuthRepositoryInterface
import com.peter.thelandlord.domain.models.Landlord
import com.peter.thelandlord.domain.models.Login

class LoginUseCase constructor(private val authRepositoryInterface: AuthRepositoryInterface) {

    operator fun invoke(
        login: Login,
        liveData: MutableLiveData<Landlord>,
        errorLiveData: MutableLiveData<String>
    ) = authRepositoryInterface.loginUser(login, liveData, errorLiveData)

}

