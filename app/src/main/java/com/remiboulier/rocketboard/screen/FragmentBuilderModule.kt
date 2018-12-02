package com.remiboulier.rocketboard.screen

import com.remiboulier.rocketboard.screen.home.HomeFragment
import com.remiboulier.rocketboard.screen.home.HomeFragmentSubComponent
import com.remiboulier.rocketboard.screen.launches.LaunchesFragment
import com.remiboulier.rocketboard.screen.launches.LaunchesFragmentSubComponent
import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap


/**
 * Created by Remi BOULIER on 02/12/2018.
 * email: boulier.r.job@gmail.com
 */
@Module
abstract class FragmentBuilderModule {

    @Binds
    @IntoMap
    @ClassKey(HomeFragment::class)
    internal abstract fun bindHomeFragmentInjectorFactory(builder: HomeFragmentSubComponent.Builder)
            : AndroidInjector.Factory<*>

    @Binds
    @IntoMap
    @ClassKey(LaunchesFragment::class)
    internal abstract fun bindLaunchesFragmentInjectorFactory(builder: LaunchesFragmentSubComponent.Builder)
            : AndroidInjector.Factory<*>
}