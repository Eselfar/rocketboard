package com.remiboulier.rocketboard

import com.remiboulier.rocketboard.screen.home.HomeFragmentSubComponent
import com.remiboulier.rocketboard.screen.home.TestItem
import com.remiboulier.rocketboard.screen.launches.LaunchesFragmentSubComponent
import com.remiboulier.rocketboard.util.ActivityScope
import dagger.Module
import dagger.Provides

@Module(subcomponents = [
    HomeFragmentSubComponent::class,
    LaunchesFragmentSubComponent::class
])
class MainActivityModule {

    @ActivityScope
    @Provides
    fun provideTestItem(): TestItem {
        return TestItem("I'm alive")
    }
}
