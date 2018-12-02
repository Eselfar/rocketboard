package com.remiboulier.rocketboard.screen.launches

import com.remiboulier.rocketboard.util.FragmentScope
import dagger.Subcomponent
import dagger.android.AndroidInjector

/**
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