package com.peter.thelandlord.domain.usecase

import com.peter.thelandlord.domain.interfaces.AuthRepositoryInterface
import com.peter.thelandlord.singleliveevent.SingleLiveEvent

class SignOutUseCase (private val authRepositoryInterface: AuthRepositoryInterface) {

    operator fun invoke(isSignedInLiveData: SingleLiveEvent<Boolean>){
        authRepositoryInterface.signOut(isSignedInLiveData)
    }

}