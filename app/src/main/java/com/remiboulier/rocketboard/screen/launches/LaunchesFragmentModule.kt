package com.remiboulier.rocketboard.screen.launches

import com.remiboulier.rocketboard.SpaceXDatabase
import com.remiboulier.rocketboard.network.SpaceXApi
import com.remiboulier.rocketboard.network.repository.LaunchRepository
import com.remiboulier.rocketboard.network.repository.LaunchRepositoryImpl
import dagger.Module
import dagger.Provides

/**
 * Created by Remi BOULIER on 02/12/2018.
 * email: boulier.r.job@gmail.com
 */

@Module
class LaunchesFragmentModule {

    @Provides
    fun provideLaunchesRepository(spaceXApi: SpaceXApi,
                                  spaceXDatabase: SpaceXDatabase)
            : LaunchRepository {
        return LaunchRepositoryImpl(spaceXApi, spaceXDatabase.launchDao())
    }
}