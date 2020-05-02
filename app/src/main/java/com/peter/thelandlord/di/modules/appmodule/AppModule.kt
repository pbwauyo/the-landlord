package com.peter.thelandlord.di.modules.appmodule

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.work.WorkManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.peter.thelandlord.data.AuthRepository
import com.peter.thelandlord.data.PropertyManagementRepoImpl
import com.peter.thelandlord.data.RentalManagementRepoImpl
import com.peter.thelandlord.data.dao.LandlordDao
import com.peter.thelandlord.data.dao.PropertyDao
import com.peter.thelandlord.data.db.AppDatabase
import com.peter.thelandlord.di.viewmodelkey.ViewModelKey
import com.peter.thelandlord.di.viewmodelproviderfactory.ViewModelProviderFactory
import com.peter.thelandlord.presentation.addproperty.PropertyViewModel
import com.peter.thelandlord.presentation.addrental.RentalViewModel
import com.peter.thelandlord.presentation.auth.AuthViewModel
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import javax.inject.Provider
import javax.inject.Singleton

@Module
object AppModule {

    @Singleton
    @Provides
    fun providesAppDatabase(application: Application): AppDatabase{
        return AppDatabase.getInstance(application.applicationContext)
    }

    @Singleton
    @Provides
    fun providesPropertyDao(appDatabase: AppDatabase): PropertyDao{
        return appDatabase.propertyDao()
    }

    @Singleton
    @Provides
    fun providesLandlordDao(appDatabase: AppDatabase): LandlordDao{
        return appDatabase.landlordDao()
    }

    @Singleton
    @Provides
    fun providesWorkManager(application: Application): WorkManager{
        return WorkManager.getInstance(application.applicationContext)
    }

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

    @Singleton
    @Provides
    fun providesPropertyManagementRepoImpl(firestore: FirebaseFirestore, firebaseAuth: FirebaseAuth,
                                           propertyDao: PropertyDao, workManager: WorkManager): PropertyManagementRepoImpl{
        return PropertyManagementRepoImpl(firestore, firebaseAuth, propertyDao, workManager)
    }

    @Singleton
    @Provides
    fun providesRentalManagementRepoImpl(firestore: FirebaseFirestore): RentalManagementRepoImpl{
        return RentalManagementRepoImpl(firestore)
    }

    @ViewModelKey(RentalViewModel::class)
    @IntoMap
    @Provides
    fun providesRentalViewModel(rentalManagementRepoImpl: RentalManagementRepoImpl): ViewModel{
        return RentalViewModel(rentalManagementRepoImpl)
    }

    @ViewModelKey(PropertyViewModel::class)
    @IntoMap
    @Provides
    fun providesPropertyViewModel(propertyManagementRepoImpl: PropertyManagementRepoImpl): ViewModel{
        return PropertyViewModel(propertyManagementRepoImpl)
    }

    @ViewModelKey(AuthViewModel::class)
    @IntoMap
    @Provides
    fun providesLoginViewModel(authRepository: AuthRepository): ViewModel{
        return AuthViewModel(authRepository)
    }
}