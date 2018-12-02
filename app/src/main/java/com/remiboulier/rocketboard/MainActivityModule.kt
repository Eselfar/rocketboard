package com.remiboulier.rocketboard

import com.remiboulier.rocketboard.screen.home.HomeFragmentSubComponent
import com.remiboulier.rocketboard.screen.launches.LaunchesFragmentSubComponent
import dagger.Module

@Module(subcomponents = [
    HomeFragmentSubComponent::class,
    LaunchesFragmentSubComponent::class
])
class MainActivityModule
