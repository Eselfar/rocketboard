package com.remiboulier.rocketboard.screen.home

import android.arch.lifecycle.LiveData
import com.remiboulier.rocketboard.network.NetworkState
import com.remiboulier.rocketboard.room.entity.RocketEntity

interface HomeFragmentViewModel {

    fun getRocketsLiveData(): LiveData<List<RocketEntity>>

    fun getNetworkState(): LiveData<NetworkState>

    fun loadRockets(forceRefresh: Boolean)

    fun isFirstTime(): Boolean

    fun updateActiveOnly(activeOnly: Boolean)
}