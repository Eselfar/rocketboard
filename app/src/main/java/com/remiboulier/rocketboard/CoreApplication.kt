package com.remiboulier.rocketboard

import android.app.Activity
import android.app.Application
import android.arch.persistence.room.Room
import com.remiboulier.rocketboard.network.SpaceXApi
import com.remiboulier.rocketboard.network.provideOkHttpClient
import com.remiboulier.rocketboard.network.provideRetrofitClient
import com.remiboulier.rocketboard.util.SpaceXApiConstants
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

    lateinit var spaceXApi: SpaceXApi
    lateinit var spaceXDB: SpaceXDatabase

    override fun onCreate() {
        super.onCreate()
        DaggerApplicationComponent.create()
                .inject(this)

        val okHttpClient = provideOkHttpClient(this)
        spaceXApi = provideRetrofitClient(SpaceXApiConstants.BASE_URL, okHttpClient)
                .create(SpaceXApi::class.java)

        spaceXDB = Room.databaseBuilder(
                applicationContext,
                SpaceXDatabase::class.java, "SpaceX_Database")
                .build()
    }

    override fun activityInjector(): AndroidInjector<Activity> {
        return dispatchingActivityInjector
    }
}