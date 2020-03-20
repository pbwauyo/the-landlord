package com.peter.thelandlord.domain.interfaces

import androidx.lifecycle.MutableLiveData
import com.peter.thelandlord.domain.models.Landlord
import com.peter.thelandlord.domain.models.Login

interface AuthRepositoryInterface {
    fun createUser(landlord: Landlord, liveData: MutableLiveData<Landlord>, errorLiveData: MutableLiveData<String>)
    fun loginUser(login: Login, liveData: MutableLiveData<Landlord>, errorLiveData: MutableLiveData<String>)
}