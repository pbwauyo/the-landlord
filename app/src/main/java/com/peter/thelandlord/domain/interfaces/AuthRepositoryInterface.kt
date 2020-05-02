package com.peter.thelandlord.domain.interfaces

import androidx.lifecycle.MutableLiveData
import com.peter.thelandlord.domain.models.Landlord
import com.peter.thelandlord.domain.models.Login
import com.peter.thelandlord.singleliveevent.SingleLiveEvent

interface AuthRepositoryInterface {

    suspend fun createUser(landlord: Landlord, errorLiveData: MutableLiveData<String>, emailLiveData: MutableLiveData<String>,
                   isRegisteringLiveData: MutableLiveData<Boolean>, isSignedInLiveData: SingleLiveEvent<Boolean>, successLiveData: MutableLiveData<String>)

    fun loginUser(login: Login, emailLiveData: MutableLiveData<String>, errorLiveData: MutableLiveData<String>,
                  loggingInLiveData: MutableLiveData<Boolean>, isSignedInLiveData: SingleLiveEvent<Boolean>)

    fun signOut(isSignedInLiveData: SingleLiveEvent<Boolean>)

    fun isUserLoggedIn(
        errorLiveData: MutableLiveData<String>,
        isSignedInLiveData: SingleLiveEvent<Boolean>,
        emailLiveData: MutableLiveData<String>
    )
}