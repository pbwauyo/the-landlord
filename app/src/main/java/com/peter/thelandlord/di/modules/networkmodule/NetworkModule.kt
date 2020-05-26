package com.peter.thelandlord.di.modules.networkmodule

import dagger.Module
import dagger.Provides
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import javax.inject.Singleton

@Module
object NetworkModule {

    @Provides
    fun providesIoExecutors(): Executor{
        return Executors.newSingleThreadExecutor()
    }

}