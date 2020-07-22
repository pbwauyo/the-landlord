package com.peter.thelandlord.presentation.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.peter.thelandlord.domain.interfaces.ProfileRepo

class ProfileViewModel(profileRepo: ProfileRepo) : ViewModel() {

    val landlordEmailLiveData = MutableLiveData<String>()
    val landlordLiveData = landlordEmailLiveData.switchMap {
        profileRepo.getLandlordProfile(it)
    }

    fun setlandlordEmail(email: String?){
        if(email != null){
            landlordEmailLiveData.value = email
        }
    }
}