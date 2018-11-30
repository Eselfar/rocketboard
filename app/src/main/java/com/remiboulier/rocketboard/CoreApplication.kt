package com.remiboulier.rocketboard

import android.app.Application
import android.arch.persistence.room.Room
import com.remiboulier.rocketboard.network.SpaceXApi
import com.remiboulier.rocketboard.network.provideOkHttpClient
import com.remiboulier.rocketboard.network.provideRetrofitClient
import com.remiboulier.rocketboard.util.SpaceXApiConstants

/**
 * Created by Remi BOULIER on 27/11/2018.
 * email: boulier.r.job@gmail.com
 */

class CoreApplication : Application() {

    lateinit var spaceXApi: SpaceXApi
    lateinit var spaceXDB: SpaceXDatabase

    override fun onCreate() {
        super.onCreate()

        val okHttpClient = provideOkHttpClient(this)
        spaceXApi = provideRetrofitClient(SpaceXApiConstants.BASE_URL, okHttpClient)
                .create(SpaceXApi::class.java)

        spaceXDB = Room.databaseBuilder(
                applicationContext,
                SpaceXDatabase::class.java, "SpaceX_Database")
                .allowMainThreadQueries()
                .build()
    }
}