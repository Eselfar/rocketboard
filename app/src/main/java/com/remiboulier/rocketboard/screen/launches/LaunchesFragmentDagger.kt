package com.remiboulier.rocketboard.screen.launches

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import com.remiboulier.rocketboard.SpaceXDatabase
import com.remiboulier.rocketboard.network.SpaceXApi
import com.remiboulier.rocketboard.network.repository.LaunchRepository
import com.remiboulier.rocketboard.network.repository.LaunchRepositoryImpl
import com.remiboulier.rocketboard.util.FragmentScope
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import dagger.android.AndroidInjector

/*
 * Created by Remi BOULIER on 02/12/2018.
 * email: boulier.r.job@gmail.com
 */

@FragmentScope
@Subcomponent(modules = [
    LaunchesFragmentModule::class
])
interface LaunchesFragmentSubComponent : AndroidInjector<LaunchesFragment> {

    @Subcomponent.Builder
    abstract class Builder : AndroidInjector.Builder<LaunchesFragment>()
}


@Module
class LaunchesFragmentModule {

    @Provides
    fun provideLaunchesRepository(spaceXApi: SpaceXApi,
                                  spaceXDatabase: SpaceXDatabase)
            : LaunchRepository {
        return LaunchRepositoryImpl(spaceXApi, spaceXDatabase.launchDao())
    }

    @Provides
    fun provideLaunchesFragmentViewModel(launchRepo: LaunchRepository,
                                         target: LaunchesFragment
    ): LaunchesFragmentViewModel =
            ViewModelProviders
                    .of(target, object : ViewModelProvider.Factory {
                        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                            @Suppress("UNCHECKED_CAST")
                            return LaunchesFragmentViewModelImpl(launchRepo) as T
                        }
                    })[LaunchesFragmentViewModelImpl::class.java]
}
