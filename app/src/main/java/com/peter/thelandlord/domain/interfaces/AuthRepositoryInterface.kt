package com.peter.thelandlord.domain.interfaces

import androidx.lifecycle.MutableLiveData
import com.peter.thelandlord.domain.models.Landlord
import com.peter.thelandlord.domain.models.Login
import com.peter.thelandlord.singleliveevent.SingleLiveEvent

interface AuthRepositoryInterface {

    fun createUser(landlord: Landlord, liveData: MutableLiveData<Landlord>, errorLiveData: MutableLiveData<String>,
                   isRegisteringLiveData: MutableLiveData<Boolean>, isSignedInLiveData: SingleLiveEvent<Boolean>, successLiveData: MutableLiveData<String>)

    fun loginUser(login: Login, liveData: MutableLiveData<Landlord>,
                  errorLiveData: MutableLiveData<String>, loggingInLiveData: MutableLiveData<Boolean>)

    fun signOut(isSignedInLiveData: SingleLiveEvent<Boolean>)

    fun isUserLoggedIn(
        liveData: MutableLiveData<Landlord>,
        errorLiveData: MutableLiveData<String>, isSignedInLiveData: SingleLiveEvent<Boolean>
    )
}