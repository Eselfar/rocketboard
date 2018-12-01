package com.remiboulier.rocketboard

import com.remiboulier.rocketboard.screen.home.TestItem
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.AndroidInjector
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap


@Module(subcomponents = [MainActivitySubComponent::class])
abstract class MainActivityModule {

    @Binds
    @IntoMap
    @ClassKey(MainActivity::class)
    abstract fun bindYourActivityInjectorFactory(builder: MainActivitySubComponent.Builder)
            : AndroidInjector.Factory<*>

    @Module
    class InjectTestItem {

        @Provides
        fun provideTestItem(): TestItem {
            return TestItem("I'm alive")
        }
    }
}
