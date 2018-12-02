package com.remiboulier.rocketboard.network.repository

import android.arch.lifecycle.LiveData
import com.remiboulier.rocketboard.network.NetworkState
import com.remiboulier.rocketboard.room.entity.RocketEntity


/**
 * Created by Remi BOULIER on 29/11/2018.
 * email: boulier.r.job@gmail.com
 */
interface RocketRepository {

    fun clear()

    fun getRockets(forceRefresh: Boolean,
                   callback: (rockets: List<RocketEntity>) -> Unit)

    fun getNetworkStateLiveData(): LiveData<NetworkState>
}
