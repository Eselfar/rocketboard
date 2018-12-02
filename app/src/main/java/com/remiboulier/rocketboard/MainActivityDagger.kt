package com.remiboulier.rocketboard

import com.remiboulier.rocketboard.screen.FragmentBuilderModule
import com.remiboulier.rocketboard.screen.home.HomeFragmentSubComponent
import com.remiboulier.rocketboard.screen.launches.LaunchesFragmentSubComponent
import com.remiboulier.rocketboard.util.ActivityScope
import dagger.Module
import dagger.Subcomponent
import dagger.android.AndroidInjector

/**
 * Created by Remi BOULIER on 01/12/2018.
 * email: boulier.r.job@gmail.com
 */

@ActivityScope
@Subcomponent(modules = [
    MainActivityModule::class,
    FragmentBuilderModule::class
])
interface MainActivitySubComponent : AndroidInjector<MainActivity> {

    @Subcomponent.Builder
    abstract class Builder : AndroidInjector.Builder<MainActivity>()
}


@Module(subcomponents = [
    HomeFragmentSubComponent::class,
    LaunchesFragmentSubComponent::class
])
class MainActivityModule