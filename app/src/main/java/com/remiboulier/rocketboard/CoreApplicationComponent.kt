package com.remiboulier.rocketboard

import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidSupportInjectionModule::class,
    CoreApplicationModule::class,
    ActivityBuilderModule::class
])
interface CoreApplicationComponent : AndroidInjector<CoreApplication> {

    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<CoreApplication>()
}