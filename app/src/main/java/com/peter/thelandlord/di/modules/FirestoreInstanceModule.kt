package com.peter.thelandlord.di.modules

import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class FirestoreInstanceModule {

    @Singleton
    @Provides
    fun providesFirestoreInstance(): FirebaseFirestore{
        return FirebaseFirestore.getInstance()
    }
}