package com.peter.thelandlord.di.modules.viewmodelmodule

import androidx.lifecycle.ViewModel
import com.peter.thelandlord.data.AuthRepository
import com.peter.thelandlord.data.PropertyManagementRepoImpl
import com.peter.thelandlord.data.RentalManagementRepoImpl
import com.peter.thelandlord.di.viewmodelkey.ViewModelKey
import com.peter.thelandlord.domain.interfaces.ProfileRepo
import com.peter.thelandlord.domain.interfaces.RentalAccountRepo
import com.peter.thelandlord.presentation.viewmodels.*
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
object ViewModelModule {

    @ViewModelKey(RentalViewModel::class)
    @IntoMap
    @Provides
    fun providesRentalViewModel(rentalManagementRepoImpl: RentalManagementRepoImpl): ViewModel {
        return RentalViewModel(
            rentalManagementRepoImpl
        )
    }

    @ViewModelKey(PropertyViewModel::class)
    @IntoMap
    @Provides
    fun providesPropertyViewModel(propertyManagementRepoImpl: PropertyManagementRepoImpl): ViewModel {
        return PropertyViewModel(
            propertyManagementRepoImpl
        )
    }

    @ViewModelKey(AuthViewModel::class)
    @IntoMap
    @Provides
    fun providesLoginViewModel(authRepository: AuthRepository): ViewModel {
        return AuthViewModel(
            authRepository
        )
    }

    @ViewModelKey(RentalAccountViewModel::class)
    @IntoMap
    @Provides
    fun providesRentalAccountViewModel(rentalAccountRepo: RentalAccountRepo): ViewModel {
        return RentalAccountViewModel(
            rentalAccountRepo
        )
    }

    @ViewModelKey(ProfileViewModel::class)
    @IntoMap
    @Provides
    fun providesProfileViewModel(profileRepo: ProfileRepo): ViewModel{
        return ProfileViewModel(profileRepo)
    }

}