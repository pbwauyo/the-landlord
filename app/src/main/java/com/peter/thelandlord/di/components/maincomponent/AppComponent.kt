package com.peter.thelandlord.di.components.maincomponent

import android.app.Application
import com.peter.thelandlord.di.baseapplication.BaseApplication
import com.peter.thelandlord.di.modules.appmodule.AppModule
import com.peter.thelandlord.di.modules.activityinjectors.LandlordActivityInjectorModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
        AndroidInjectionModule::class,
        AppModule::class,
        LandlordActivityInjectorModule::class
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