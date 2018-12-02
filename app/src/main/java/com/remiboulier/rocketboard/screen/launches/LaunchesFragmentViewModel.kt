package com.remiboulier.rocketboard.screen.launches

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.github.mikephil.charting.data.BarEntry
import com.remiboulier.rocketboard.network.repository.LaunchRepository
import com.remiboulier.rocketboard.room.entity.LaunchEntity

/**
 * Created by Remi BOULIER on 27/11/2018.
 * email: boulier.r.job@gmail.com
 */

class LaunchesFragmentViewModel(private val launchesRepo: LaunchRepository,
                                private val rocketId: String)
    : ViewModel() {

    val launchesLiveData = MutableLiveData<List<LaunchEntity>>()
    val launchesPerYearLiveData = MutableLiveData<List<BarEntry>>()
    val networkState = launchesRepo.getNetworkStateLiveData()

    override fun onCleared() {
        super.onCleared()
        launchesRepo.clear()
    }

    fun loadLaunches(forceRefresh: Boolean) {
        launchesRepo.getLaunches(rocketId, forceRefresh) {
            launchesLiveData.postValue(it)
            launchesPerYearLiveData.postValue(generateLaunchesPerYearMap(it))
        }
    }

    fun generateLaunchesPerYearMap(launches: List<LaunchEntity>): List<BarEntry> =
            mutableListOf<BarEntry>().apply {
                val map = launches.groupingBy { it.launchYear }.eachCount()
                for (entry in map) {
                    add(BarEntry(entry.key.toFloat(), entry.value.toFloat()))
                }
            }
}