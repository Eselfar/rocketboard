package com.remiboulier.rocketboard

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidInjectionModule::class,
    CoreApplicationModule::class,
    MainActivityModule::class])
interface CoreApplicationComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): CoreApplicationComponent
    }

    fun inject(application: CoreApplication)
}