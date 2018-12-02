package com.remiboulier.rocketboard

import com.remiboulier.rocketboard.network.SpaceXApi
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import javax.inject.Inject


/**
 * Created by Remi BOULIER on 27/11/2018.
 * email: boulier.r.job@gmail.com
 */

class CoreApplication : DaggerApplication() {

    @Inject
    lateinit var spaceXApi: SpaceXApi

    @Inject
    lateinit var spaceXDB: SpaceXDatabase

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerCoreApplicationComponent.builder().create(this)
    }
}