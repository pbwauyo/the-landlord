package com.peter.thelandlord.presentation.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.peter.thelandlord.data.AuthRepository
import com.peter.thelandlord.domain.models.Landlord
import com.peter.thelandlord.domain.models.Login
import com.peter.thelandlord.domain.usecase.CheckSignedInUserUseCase
import com.peter.thelandlord.domain.usecase.LoginUseCase
import com.peter.thelandlord.domain.usecase.RegisterUseCase
import com.peter.thelandlord.domain.usecase.SignOutUseCase
import com.peter.thelandlord.extensions.stringextensions.*
import com.peter.thelandlord.singleliveevent.SingleLiveEvent

class AuthViewModel constructor(authRepository: AuthRepository) : ViewModel(){

    //SIGN UP LIVE DATA
    companion object {
        const val EMPTY_FIELD_ERROR = "field cannot be empty"
        const val FIELD_MATCH_ERROR = "fields must match"
    }

    val signUpFNameErrorLiveData = MutableLiveData<String>()
    val signUpLNameErrorLiveData = MutableLiveData<String>()
    val signUpEmailErrorLiveData = MutableLiveData<String>()
    val signUpPswdErrorLiveData = MutableLiveData<String>()
    val signUpConfPswdErrorLiveData = MutableLiveData<String>()
    val signUpPswdMatchErrorLiveData = MutableLiveData<String>()
    val signUpSuccessLiveData = SingleLiveEvent<String>()

    val signUpFNameLiveData = MutableLiveData<String>()
    val signUpLNameLiveData = MutableLiveData<String>()
    val signUpEmailLiveData = MutableLiveData<String>()
    val signUpPswdLiveData = MutableLiveData<String>()
    val signUpConfPswdLiveData = MutableLiveData<String>()
    val isRegisteringLiveData = MutableLiveData<Boolean>()

    //LOGIN LIVE DATA
    val emailLiveData = MutableLiveData<String>()
    val passwordLiveData = MutableLiveData<String>()
    val errorLiveData = SingleLiveEvent<String>()
    val loggingInLiveData = MutableLiveData<Boolean>()
    val isSignedInLiveData = SingleLiveEvent<Boolean>()

    val emailErrorLiveData = MutableLiveData<String>()
    val pswdErrorLiveData = MutableLiveData<String>()

    private val _landlordLiveData = MutableLiveData<Landlord>()

    val landlordLiveData: LiveData<Landlord>    //exposed livedata
        get() = _landlordLiveData

    //use cases
    private val loginUser = LoginUseCase(authRepository)
    private val checkSignedInUser = CheckSignedInUserUseCase(authRepository)
    private val registerUser = RegisterUseCase(authRepository)
    private val signOut = SignOutUseCase(authRepository)

    fun login() {
        var fieldsAreEmpty: Boolean

        if (!emailLiveData.value.isNullableStringValid()){
            emailErrorLiveData.value = EMPTY_FIELD_ERROR
            fieldsAreEmpty = true
        }
        else{
            fieldsAreEmpty = false
        }

        if (!passwordLiveData.value.isNullableStringValid()){
            pswdErrorLiveData.value = EMPTY_FIELD_ERROR
            fieldsAreEmpty = true
        }
        else{
            fieldsAreEmpty = false
        }

        if (!fieldsAreEmpty){
            loggingInLiveData.value = true
            val loginDetails = Login(emailLiveData.value!!.trim(), passwordLiveData.value!!.trim())
            loginUser(loginDetails, _landlordLiveData, errorLiveData, loggingInLiveData)
        }

    }

    fun register(){
        var fieldsAreEmpty:Boolean

        if (!signUpFNameLiveData.value.isNullableStringValid()){
            signUpFNameErrorLiveData.value = EMPTY_FIELD_ERROR
            fieldsAreEmpty = true
        }
        else{
            fieldsAreEmpty = false
        }

        if (!signUpLNameLiveData.value.isNullableStringValid()){
            signUpLNameErrorLiveData.value = EMPTY_FIELD_ERROR
            fieldsAreEmpty = true
        }
        else{
            fieldsAreEmpty = false
        }

        if (!signUpEmailLiveData.value.isNullableStringValid()){
            signUpEmailErrorLiveData.value = EMPTY_FIELD_ERROR
            fieldsAreEmpty = true
        }
        else{
            fieldsAreEmpty = false
        }

        if (!signUpPswdLiveData.value.isNullableStringValid()){
            signUpPswdErrorLiveData.value = EMPTY_FIELD_ERROR
            fieldsAreEmpty = true
        }
        else{
            fieldsAreEmpty = false
        }

        if (!signUpConfPswdLiveData.value.isNullableStringValid()){
            signUpConfPswdErrorLiveData.value = EMPTY_FIELD_ERROR
            fieldsAreEmpty = true
        }
        else{
            fieldsAreEmpty = false
        }

        if(!fieldsAreEmpty){
            //check if passwords match
            if (signUpPswdLiveData.value == signUpConfPswdLiveData.value){
                isRegisteringLiveData.value = true

                val landlord = Landlord(signUpEmailLiveData.value?.trim()!!, signUpFNameLiveData.value?.trim()!!,
                    signUpLNameLiveData.value?.trim()!!, signUpPswdLiveData.value?.trim()!!)

                registerUser(landlord, _landlordLiveData, errorLiveData, isRegisteringLiveData, isSignedInLiveData, signUpSuccessLiveData)
            }else{
                signUpPswdMatchErrorLiveData.value = FIELD_MATCH_ERROR
            }
        }

    }

    fun signOutUser(){
        signOut(isSignedInLiveData)
    }

    fun checkedSignedInUser(){
        checkSignedInUser(_landlordLiveData, errorLiveData, isSignedInLiveData)
    }

    fun setSignedInStatus(value: Boolean){
        isSignedInLiveData.value = value
    }

    private fun clearSignUpFields(){
        signUpLNameLiveData.value = ""
        signUpFNameLiveData.value = ""
        signUpEmailLiveData.value = ""
        signUpPswdLiveData.value = ""
        signUpConfPswdLiveData.value = ""
    }

    private fun clearLoginFields(){
        emailLiveData.value = ""
        passwordLiveData.value = ""
    }

    override fun toString(): String {
        return "AuthViewModel Created"
    }
}