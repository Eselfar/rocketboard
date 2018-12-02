package com.remiboulier.rocketboard

import android.arch.persistence.room.Room
import android.content.Context
import com.remiboulier.rocketboard.network.SpaceXApi
import com.remiboulier.rocketboard.network.provideOkHttpClient
import com.remiboulier.rocketboard.network.provideRetrofitClient
import com.remiboulier.rocketboard.util.SharedPreferencesHelper
import com.remiboulier.rocketboard.util.SharedPreferencesHelperImpl
import com.remiboulier.rocketboard.util.SpaceXApiConstants
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


/**
 * Created by Remi BOULIER on 01/12/2018.
 * email: boulier.r.job@gmail.com
 */

@Module(subcomponents = [
    MainActivitySubComponent::class
])
class CoreApplicationModule {

    @Provides
    @Singleton
    fun provideContext(application: CoreApplication): Context {
        return application
    }

    @Singleton
    @Provides
    fun provideSpaceXAPI(context: Context): SpaceXApi {
        val okHttpClient = provideOkHttpClient(context)
        return provideRetrofitClient(SpaceXApiConstants.BASE_URL, okHttpClient)
                .create(SpaceXApi::class.java)
    }

    @Singleton
    @Provides
    fun provideSpaceXDB(applicationContext: Context): SpaceXDatabase {
        return Room.databaseBuilder(
                applicationContext,
                SpaceXDatabase::class.java, "SpaceX_Database")
                .build()
    }

    @Singleton
    @Provides
    fun provideSharedPreferenceHelper(context: Context): SharedPreferencesHelper {
        return SharedPreferencesHelperImpl(context)
    }
}