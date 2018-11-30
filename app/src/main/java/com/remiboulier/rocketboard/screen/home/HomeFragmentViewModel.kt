package com.remiboulier.rocketboard.screen.home

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.remiboulier.rocketboard.network.repository.RocketRepository
import com.remiboulier.rocketboard.room.entity.RocketEntity
import com.remiboulier.rocketboard.util.SharedPreferencesHelper

/**
 * Created by Remi BOULIER on 27/11/2018.
 * email: boulier.r.job@gmail.com
 */

class HomeFragmentViewModel(private val rocketRepo: RocketRepository,
                            private val prefsHelper: SharedPreferencesHelper) : ViewModel() {

    val rocketsLiveData = MutableLiveData<List<RocketEntity>>()
    val networkState = rocketRepo.networkState

    private var activeOnly = false

    private var rockets = listOf<RocketEntity>()

    override fun onCleared() {
        super.onCleared()
        rocketRepo.clear()
    }

    fun loadRockets(forceRefresh: Boolean) {
        rocketRepo.getRockets(forceRefresh) {
            rockets = it
            rocketsLiveData.postValue(filterResults(it, activeOnly))
        }
    }

    fun isFirstTime(): Boolean = prefsHelper.getIsFirstTime().also {
        if (it) prefsHelper.storeIsFirstTime(false)
    }

    fun updateActiveOnly(activeOnly: Boolean) {
        this.activeOnly = activeOnly
        rocketsLiveData.postValue(filterResults(rockets, activeOnly))
    }

    fun filterResults(rockets: List<RocketEntity>, active: Boolean): List<RocketEntity> =
            if (active) rockets.filter { rocket -> rocket.active }.toMutableList()
            else rockets
}