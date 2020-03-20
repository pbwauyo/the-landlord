package com.peter.thelandlord.presentation.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.peter.thelandlord.data.AuthRepository
import com.peter.thelandlord.domain.models.Landlord
import com.peter.thelandlord.domain.models.Login
import com.peter.thelandlord.domain.usecase.LoginUseCase
import com.peter.thelandlord.extensions.stringextensions.*
import com.peter.thelandlord.singleliveevent.SingleLiveEvent

class UserLoginViewModel constructor( authRepository: AuthRepository) : ViewModel(){

    val emailLiveData = MutableLiveData<String>()

    val passwordLiveData = MutableLiveData<String>()

    val errorLiveData = SingleLiveEvent<String>()

    private val _landlordLiveData = SingleLiveEvent<Landlord>()

    val landlordLiveData: LiveData<Landlord>    //exposed livedata
        get() = _landlordLiveData

    private val loginUseCase = LoginUseCase(authRepository)

    fun login(){
        if (emailLiveData.value.isNullableStringValid() && passwordLiveData.value.isNullableStringValid()){
            val loginDetails = Login(emailLiveData.value!!.trim(), passwordLiveData.value!!.trim())
            loginUseCase(loginDetails, _landlordLiveData, errorLiveData)
        }
    }

}