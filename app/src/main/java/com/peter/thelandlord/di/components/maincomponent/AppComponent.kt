package com.peter.thelandlord.di.components.maincomponent

import android.app.Application
import com.peter.thelandlord.di.baseapplication.BaseApplication
import com.peter.thelandlord.di.modules.appmodule.AppModule
import com.peter.thelandlord.di.modules.activityinjectors.LandlordActivityInjectorModule
import com.peter.thelandlord.di.modules.activityinjectors.MainActivityInjectorModule
import com.peter.thelandlord.di.modules.fragmentinjectors.FragmentModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(modules = [
        AndroidInjectionModule::class,
        AppModule::class,
        LandlordActivityInjectorModule::class,
        MainActivityInjectorModule::class,
        FragmentModule::class
    ]
)
interface AppComponent{
    fun inject(app: BaseApplication)

    @Component.Builder
    interface Builder{

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

}