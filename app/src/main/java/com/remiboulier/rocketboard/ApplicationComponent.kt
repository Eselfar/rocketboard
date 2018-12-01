package com.remiboulier.rocketboard

import dagger.Component
import dagger.android.AndroidInjectionModule


@Component(modules = [
    AndroidInjectionModule::class,
    MainActivityModule::class])
interface ApplicationComponent {

    fun inject(application: CoreApplication)
}