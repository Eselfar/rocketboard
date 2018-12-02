package com.remiboulier.rocketboard.screen.launches

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.github.mikephil.charting.data.BarEntry
import com.remiboulier.rocketboard.network.repository.LaunchRepository
import com.remiboulier.rocketboard.room.entity.LaunchEntity

/*
 * Created by Remi BOULIER on 02/12/2018.
 * email: boulier.r.job@gmail.com
 */

class LaunchesFragmentViewModelImpl(private val launchesRepo: LaunchRepository)
    : ViewModel(), LaunchesFragmentViewModel {

    private val launchesLiveData = MutableLiveData<List<LaunchEntity>>()
    private val launchesPerYearLiveData = MutableLiveData<List<BarEntry>>()
    private val networkState = launchesRepo.getNetworkStateLiveData()

    override fun getLaunchesLiveData() = launchesLiveData

    override fun getLaunchesPerYearLiveData() = launchesPerYearLiveData

    override fun getNetworkState() = networkState

    override fun onCleared() {
        super.onCleared()
        launchesRepo.clear()
    }

    override fun loadLaunches(rocketId: String, forceRefresh: Boolean) {
        launchesRepo.getLaunches(rocketId, forceRefresh) {
            launchesLiveData.postValue(it)
            launchesPerYearLiveData.postValue(generateLaunchesPerYearMap(it))
        }
    }

    override fun generateLaunchesPerYearMap(launches: List<LaunchEntity>): List<BarEntry> =
            mutableListOf<BarEntry>().apply {
                val map = launches.groupingBy { it.launchYear }.eachCount()
                for (entry in map) {
                    add(BarEntry(entry.key.toFloat(), entry.value.toFloat()))
                }
            }

}