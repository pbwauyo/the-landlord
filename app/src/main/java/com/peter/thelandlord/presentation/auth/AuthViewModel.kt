package com.peter.thelandlord.presentation.auth

import androidx.lifecycle.*
import com.peter.thelandlord.data.AuthRepository
import com.peter.thelandlord.domain.models.Landlord
import com.peter.thelandlord.domain.models.Login
import com.peter.thelandlord.domain.usecases.authusecases.CheckSignedInUserUseCase
import com.peter.thelandlord.domain.usecases.authusecases.LoginUseCase
import com.peter.thelandlord.domain.usecases.authusecases.RegisterUseCase
import com.peter.thelandlord.domain.usecases.authusecases.SignOutUseCase
import com.peter.thelandlord.extensions.stringextensions.*
import com.peter.thelandlord.singleliveevent.SingleLiveEvent
import io.reactivex.Completable
import kotlinx.coroutines.launch

class AuthViewModel constructor(val authRepository: AuthRepository) : ViewModel(){

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

    val currentUserEmailLiveData = MutableLiveData<String>()

    val emailErrorLiveData = MutableLiveData<String>()
    val pswdErrorLiveData = MutableLiveData<String>()

    private val _landlordLiveData = Transformations.switchMap(currentUserEmailLiveData) {
        authRepository.getLandlordByEmail(it, errorLiveData)
    }

    val landlordLiveData: LiveData<Landlord>    //exposed livedata
        get() = _landlordLiveData

    //use cases
    private val loginUser =
        LoginUseCase(
            authRepository
        )
    private val checkSignedInUser =
        CheckSignedInUserUseCase(
            authRepository
        )
    private val registerUser =
        RegisterUseCase(
            authRepository
        )
    private val signOut =
        SignOutUseCase(
            authRepository
        )

    fun login() {
        var isEmailFieldEmpty = true
        var isPswdFieldEmpty = true

        if (!emailLiveData.value.isNullableStringValid()){
            emailErrorLiveData.value = EMPTY_FIELD_ERROR
        }
        else{
            isEmailFieldEmpty = false
        }

        if (!passwordLiveData.value.isNullableStringValid()){
            pswdErrorLiveData.value = EMPTY_FIELD_ERROR
        }
        else{
            isPswdFieldEmpty = false
        }

        if (!(isEmailFieldEmpty || isPswdFieldEmpty)){
            loggingInLiveData.value = true
            val loginDetails = Login(emailLiveData.value!!.trim(), passwordLiveData.value!!.trim())
            loginUser(loginDetails, currentUserEmailLiveData, errorLiveData, loggingInLiveData, isSignedInLiveData)
        }

    }

    fun register(){

        var isFNameFieldEmpty = true
        var isLNameFieldEmpty = true
        var isEmailFieldEmpty = true
        var isPswdFieldEmpty = true
        var isConfPswdFieldEmpty = true

        if (!signUpFNameLiveData.value.isNullableStringValid()){
            signUpFNameErrorLiveData.value = EMPTY_FIELD_ERROR
        }
        else{
            isFNameFieldEmpty = false
        }

        if (!signUpLNameLiveData.value.isNullableStringValid()){
            signUpLNameErrorLiveData.value = EMPTY_FIELD_ERROR
        }
        else{
            isLNameFieldEmpty = false
        }

        if (!signUpEmailLiveData.value.isNullableStringValid()){
            signUpEmailErrorLiveData.value = EMPTY_FIELD_ERROR
        }
        else{
            isEmailFieldEmpty = false
        }

        if (!signUpPswdLiveData.value.isNullableStringValid()){
            signUpPswdErrorLiveData.value = EMPTY_FIELD_ERROR
        }
        else{
            isPswdFieldEmpty = false
        }

        if (!signUpConfPswdLiveData.value.isNullableStringValid()){
            signUpConfPswdErrorLiveData.value = EMPTY_FIELD_ERROR
        }
        else{
            isConfPswdFieldEmpty = false
        }

        if(!(isFNameFieldEmpty || isLNameFieldEmpty || isEmailFieldEmpty || isPswdFieldEmpty || isConfPswdFieldEmpty)){
            //check if passwords match
            if (signUpPswdLiveData.value!!.trim() == signUpConfPswdLiveData.value!!.trim()){
                isRegisteringLiveData.value = true

                val landlord = Landlord(signUpEmailLiveData.value?.trim()!!, signUpFNameLiveData.value?.trim()!!,
                    signUpLNameLiveData.value?.trim()!!, signUpPswdLiveData.value?.trim()!!)

                viewModelScope.launch {
                    registerUser(landlord, errorLiveData, currentUserEmailLiveData,
                        isRegisteringLiveData, isSignedInLiveData, signUpSuccessLiveData)
                }

            }else{
                signUpPswdMatchErrorLiveData.postValue(FIELD_MATCH_ERROR)
            }
        }

    }

    //fun saveLandlordToDB(landlord: Landlord) = authRepository.landlordDao.saveLandlord(landlord)

    fun signOutUser(){
        signOut(isSignedInLiveData)
    }

    fun checkedSignedInUser(){
        checkSignedInUser(errorLiveData, isSignedInLiveData, currentUserEmailLiveData)
    }

    fun setSignedInStatus(value: Boolean){
        isSignedInLiveData.postValue(value)

    }

    fun clearSignUpFields(){
        signUpLNameLiveData.postValue("")
        signUpFNameLiveData.postValue("")
        signUpEmailLiveData.postValue("")
        signUpPswdLiveData.postValue("")
        signUpConfPswdLiveData.postValue("")
    }

    fun clearLoginFields(){
        emailLiveData.postValue("")
        passwordLiveData.postValue("")
    }

    fun setCurrentUserEmail(value: String){
        currentUserEmailLiveData.postValue(value)
    }

    override fun toString(): String {
        return "AuthViewModel Created"
    }
}