package com.peter.thelandlord.di.modules

import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class FirebaseAuthModule {

    @Singleton
    @Provides
    fun providesFirebaseAuthInstance(): FirebaseAuth{
        return FirebaseAuth.getInstance()
    }
}