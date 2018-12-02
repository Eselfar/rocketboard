package com.remiboulier.rocketboard.network.repository

import android.arch.lifecycle.LiveData
import com.remiboulier.rocketboard.network.NetworkState
import com.remiboulier.rocketboard.room.entity.LaunchEntity

/**
 * Created by Remi BOULIER on 30/11/2018.
 * email: boulier.r.job@gmail.com
 */

interface LaunchRepository {

    fun clear()

    fun getLaunches(rocketId: String,
                    forceRefresh: Boolean,
                    callback: (launches: List<LaunchEntity>) -> Unit)

    fun getNetworkStateLiveData(): LiveData<NetworkState>
}
