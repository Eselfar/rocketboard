package com.remiboulier.rocketboard

import com.remiboulier.rocketboard.screen.home.TestItem
import com.remiboulier.rocketboard.util.ActivityScope
import dagger.Module
import dagger.Provides

@Module()
class MainActivityModule {

    @ActivityScope
    @Provides
    fun provideTestItem(): TestItem {
        return TestItem("I'm alive")
    }
}
