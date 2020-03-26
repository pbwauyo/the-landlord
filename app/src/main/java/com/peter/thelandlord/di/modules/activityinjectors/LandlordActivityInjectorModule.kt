package com.peter.thelandlord.di.modules.activityinjectors

import com.peter.thelandlord.presentation.landlordactivity.LandlordActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class LandlordActivityInjectorModule {
    @ContributesAndroidInjector
    abstract fun landlordActivityInjector(): LandlordActivity
}