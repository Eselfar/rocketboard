package com.remiboulier.rocketboard.screen.home

import com.remiboulier.rocketboard.util.FragmentScope
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