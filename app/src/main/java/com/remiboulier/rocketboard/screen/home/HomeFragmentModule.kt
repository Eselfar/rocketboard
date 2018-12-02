package com.remiboulier.rocketboard.screen.home

import com.remiboulier.rocketboard.SpaceXDatabase
import com.remiboulier.rocketboard.network.SpaceXApi
import com.remiboulier.rocketboard.network.repository.RocketRepository
import com.remiboulier.rocketboard.network.repository.RocketRepositoryImpl
import dagger.Module
import dagger.Provides

@Module
class HomeFragmentModule {

    @Provides
    fun provideRocketRepository(spaceXApi: SpaceXApi,
                                spaceXDatabase: SpaceXDatabase)
            : RocketRepository {
        return RocketRepositoryImpl(spaceXApi, spaceXDatabase.rocketDao())
    }
}
