package com.remiboulier.rocketboard

import android.app.Activity
import android.app.Application
import com.remiboulier.rocketboard.network.SpaceXApi
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject


/**
 * Created by Remi BOULIER on 27/11/2018.
 * email: boulier.r.job@gmail.com
 */

class CoreApplication : Application(), HasActivityInjector {

    @Inject
    lateinit var dispatchingActivityInjector: DispatchingAndroidInjector<Activity>

    @Inject
    lateinit var spaceXApi: SpaceXApi
    @Inject
    lateinit var spaceXDB: SpaceXDatabase

    override fun onCreate() {
        DaggerCoreApplicationComponent.builder()
                .application(this)
                .build()
                .inject(this)
        super.onCreate()
    }

    override fun activityInjector(): AndroidInjector<Activity> {
        return dispatchingActivityInjector
    }
}