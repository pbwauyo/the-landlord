package com.peter.thelandlord.di.modules.appmodule

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.work.WorkManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.peter.thelandlord.data.PropertyManagementRepoImpl
import com.peter.thelandlord.data.RentalManagementRepoImpl
import com.peter.thelandlord.data.dao.LandlordDao
import com.peter.thelandlord.data.dao.PropertyDao
import com.peter.thelandlord.data.dao.RentalDao
import com.peter.thelandlord.data.db.AppDatabase
import com.peter.thelandlord.di.viewmodelproviderfactory.ViewModelProviderFactory
import dagger.Module
import dagger.Provides
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
    fun providesRentalsDao(appDatabase: AppDatabase): RentalDao = appDatabase.rentalDao()

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
    fun providesRentalManagementRepoImpl(firestore: FirebaseFirestore, appDatabase: AppDatabase, workManager: WorkManager): RentalManagementRepoImpl{
        return RentalManagementRepoImpl(firestore, appDatabase, workManager)
    }
}