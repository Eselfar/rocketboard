package com.remiboulier.rocketboard.screen.launches

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.github.mikephil.charting.data.BarEntry
import com.remiboulier.rocketboard.network.NetworkState
import com.remiboulier.rocketboard.room.entity.LaunchEntity

/**
 * Created by Remi BOULIER on 27/11/2018.
 * email: boulier.r.job@gmail.com
 */

interface LaunchesFragmentViewModel {

    fun getLaunchesLiveData(): MutableLiveData<List<LaunchEntity>>

    fun getLaunchesPerYearLiveData(): MutableLiveData<List<BarEntry>>

    fun getNetworkState(): LiveData<NetworkState>

    fun loadLaunches(rocketId: String, forceRefresh: Boolean)

    fun generateLaunchesPerYearMap(launches: List<LaunchEntity>): List<BarEntry>
}