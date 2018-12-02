package com.remiboulier.rocketboard

import dagger.android.AndroidInjector
import dagger.android.DaggerApplication


/**
 * Created by Remi BOULIER on 27/11/2018.
 * email: boulier.r.job@gmail.com
 */

class CoreApplication : DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerCoreApplicationComponent.builder()
                .create(this)
    }
}