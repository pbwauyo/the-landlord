package com.peter.thelandlord.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.peter.thelandlord.data.AuthRepository
import javax.inject.Inject
import javax.inject.Singleton

@Suppress("UNCHECKED_CAST")
@Singleton
class UserLoginVMFactory @Inject constructor(var authRepository: AuthRepository): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return UserLoginViewModel(authRepository) as T
    }

}