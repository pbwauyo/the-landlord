package com.peter.thelandlord.di.modules.appmodule

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.peter.thelandlord.data.AuthRepository
import com.peter.thelandlord.di.viewmodelkey.ViewModelKey
import com.peter.thelandlord.di.viewmodelproviderfactory.ViewModelProviderFactory
import com.peter.thelandlord.presentation.auth.UserLoginViewModel
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import javax.inject.Provider
import javax.inject.Singleton

@Module
object AppModule {

    @Singleton
    @Provides
    fun providesFirebaseAuthInstance(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Singleton
    @Provides
    fun providesViewModelProviderFactory(
        providers: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>)
            : ViewModelProviderFactory = ViewModelProviderFactory(providers)

    @Singleton
    @Provides
    fun providesFirestoreInstance(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @ViewModelKey(UserLoginViewModel::class)
    @IntoMap
    @Provides
    fun providesLoginViewModel(authRepository: AuthRepository): ViewModel{
        return UserLoginViewModel(authRepository)
    }
}