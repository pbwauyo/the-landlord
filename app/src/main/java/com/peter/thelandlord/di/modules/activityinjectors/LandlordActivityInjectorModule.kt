package com.peter.thelandlord.di.modules.activityinjectors

import com.peter.thelandlord.di.modules.fragmentinjectors.FragmentModule
import com.peter.thelandlord.presentation.LandlordActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class LandlordActivityInjectorModule {
    @ContributesAndroidInjector(modules = [FragmentModule::class])
    abstract fun landlordActivityInjector(): LandlordActivity
}