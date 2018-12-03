package com.remiboulier.rocketboard.screen.home

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.remiboulier.rocketboard.network.NetworkState
import com.remiboulier.rocketboard.network.repository.RocketRepository
import com.remiboulier.rocketboard.room.entity.RocketEntity
import com.remiboulier.rocketboard.util.SharedPrefsHelper

/**
 * Created by Remi BOULIER on 27/11/2018.
 * email: boulier.r.job@gmail.com
 */

open class HomeFragmentViewModelImpl(private val rocketRepo: RocketRepository,
                                     private val prefsHelper: SharedPrefsHelper)
    : ViewModel(), HomeFragmentViewModel {

    private val rocketsLiveData = MutableLiveData<List<RocketEntity>>()
    private val networkState = rocketRepo.getNetworkStateLiveData()

    var activeOnly = false
    var rockets = listOf<RocketEntity>()

    override fun getRocketsLiveData(): LiveData<List<RocketEntity>> = rocketsLiveData

    override fun getNetworkState(): LiveData<NetworkState> = networkState

    override fun onCleared() {
        super.onCleared()
        rocketRepo.clear()
    }

    override fun loadRockets(forceRefresh: Boolean) {
        if (rockets.isNotEmpty() && !forceRefresh) {
            updateLiveData(rockets, activeOnly)
        } else {
            getDataFromRepo(forceRefresh)
        }
    }

    override fun isFirstTime(): Boolean = prefsHelper.getIsFirstTime().also {
        if (it) prefsHelper.storeIsFirstTime(false)
    }

    override fun updateActiveOnly(activeOnly: Boolean) {
        if (activeOnly != this.activeOnly) {
            this.activeOnly = activeOnly
            updateLiveData(rockets, activeOnly)
        }
    }

    fun getDataFromRepo(forceRefresh: Boolean) =
            rocketRepo.getRockets(forceRefresh) {
                rockets = it
                updateLiveData(rockets, activeOnly)
            }

    fun updateLiveData(rockets: List<RocketEntity>, activeOnly: Boolean) {
        rocketsLiveData.postValue(filterResults(rockets, activeOnly))
    }

    fun filterResults(rockets: List<RocketEntity>, active: Boolean): List<RocketEntity> =
            if (active) rockets.filter { rocket -> rocket.active }.toMutableList()
            else rockets
}

