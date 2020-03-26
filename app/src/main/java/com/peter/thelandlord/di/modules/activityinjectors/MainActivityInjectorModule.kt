package com.peter.thelandlord.di.modules.activityinjectors

import com.peter.thelandlord.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainActivityInjectorModule {
    @ContributesAndroidInjector
    abstract fun mainActivityInjector(): MainActivity
}