package com.peter.thelandlord.di.modules.reposmodule

import com.peter.thelandlord.data.ProfileRepoImpl
import com.peter.thelandlord.data.RentalAccountRepoImpl
import com.peter.thelandlord.domain.interfaces.ProfileRepo
import com.peter.thelandlord.domain.interfaces.RentalAccountRepo
import dagger.Binds
import dagger.Module

@Module
abstract class RepoModule {

    @Binds
    abstract fun providesRentalAccountRepoImpl(rentalAccountRepoImpl: RentalAccountRepoImpl): RentalAccountRepo

    @Binds
    abstract fun providesProfileRepoImpl(profileRepoImpl: ProfileRepoImpl): ProfileRepo
}