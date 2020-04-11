package com.peter.thelandlord.di.baseapplication

import androidx.multidex.MultiDexApplication
import com.peter.thelandlord.di.components.maincomponent.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class BaseApplication : MultiDexApplication(),  HasAndroidInjector{

    @Inject lateinit var androidInjector: DispatchingAndroidInjector<Any>

    override fun onCreate() {
        super.onCreate()

        DaggerAppComponent.builder().application(this).build().inject(this)
    }

    override fun androidInjector(): AndroidInjector<Any>? {
        return androidInjector
    }

}