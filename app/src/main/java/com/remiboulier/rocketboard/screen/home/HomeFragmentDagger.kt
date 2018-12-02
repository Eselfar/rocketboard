package com.remiboulier.rocketboard.screen.home

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import com.remiboulier.rocketboard.SpaceXDatabase
import com.remiboulier.rocketboard.network.SpaceXApi
import com.remiboulier.rocketboard.network.repository.RocketRepository
import com.remiboulier.rocketboard.network.repository.RocketRepositoryImpl
import com.remiboulier.rocketboard.util.FragmentScope
import com.remiboulier.rocketboard.util.SharedPreferencesHelper
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import dagger.android.AndroidInjector

/**
 * Created by Remi BOULIER on 02/12/2018.
 * email: boulier.r.job@gmail.com
 */

@FragmentScope
@Subcomponent(modules = [
    HomeFragmentModule::class
])
interface HomeFragmentSubComponent : AndroidInjector<HomeFragment> {

    @Subcomponent.Builder
    abstract class Builder : AndroidInjector.Builder<HomeFragment>()
}


@Module
class HomeFragmentModule {

    @Provides
    fun provideRocketRepository(spaceXApi: SpaceXApi,
                                spaceXDatabase: SpaceXDatabase)
            : RocketRepository {
        return RocketRepositoryImpl(spaceXApi, spaceXDatabase.rocketDao())
    }

    @Provides
    fun provideHomeFragmentViewModel(rocketRepo: RocketRepository,
                                     prefsHelper: SharedPreferencesHelper,
                                     target: HomeFragment
    ): HomeFragmentViewModel =
            ViewModelProviders
                    .of(target, object : ViewModelProvider.Factory {
                        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                            @Suppress("UNCHECKED_CAST")
                            return HomeFragmentViewModel(rocketRepo, prefsHelper) as T
                        }
                    }).get(HomeFragmentViewModel::class.java)
}