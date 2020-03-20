package com.peter.thelandlord.di.components

import android.app.Application
import androidx.multidex.MultiDexApplication
import com.peter.thelandlord.presentation.auth.MainActivity
import com.peter.thelandlord.di.modules.FirebaseAuthModule
import com.peter.thelandlord.di.modules.FirestoreInstanceModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [FirebaseAuthModule::class, FirestoreInstanceModule::class])
interface ApplicationComponent{
    fun inject(activity: MainActivity)
}

class LandlordApplication : MultiDexApplication() {

    val applicationComponent = DaggerApplicationComponent.create()
}