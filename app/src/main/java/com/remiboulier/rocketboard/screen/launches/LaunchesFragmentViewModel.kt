package com.remiboulier.rocketboard.screen.launches

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.github.mikephil.charting.data.BarEntry
import com.remiboulier.rocketboard.network.SpaceXApi
import com.remiboulier.rocketboard.network.dto.LaunchDto
import com.remiboulier.rocketboard.network.repository.NetworkState
import com.remiboulier.rocketboard.util.getErrorMessage
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by Remi BOULIER on 27/11/2018.
 * email: boulier.r.job@gmail.com
 */

class LaunchesFragmentViewModel(private val spaceXApi: SpaceXApi,
                                private val rocketId: String)
    : ViewModel() {

    val launchesLiveData = MutableLiveData<MutableList<LaunchDto>>()
    val networkState = MutableLiveData<NetworkState>()
    val launchesPerYearLiveData = MutableLiveData<List<BarEntry>>()

    private val disposables = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }

    fun loadLaunches() {
        networkState.postValue(NetworkState.LOADING)

        disposables.add(spaceXApi.getLaunches()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap { res -> Observable.fromIterable(res) }
                .filter { launch -> launch.rocket.rocketId == rocketId }
                .toList()
                .subscribe(
                        { res ->
                            launchesLiveData.postValue(res)
                            launchesPerYearLiveData.postValue(generateLaunchesPerYearMap(res))
                            networkState.postValue(NetworkState.LOADED)
                        },
                        { t ->
                            t.printStackTrace()
                            networkState.postValue(NetworkState.error(getErrorMessage(t)))
                        }))
    }

    fun generateLaunchesPerYearMap(launches: MutableList<LaunchDto>): List<BarEntry> =
            mutableListOf<BarEntry>().apply {
                val map = launches.groupingBy { it.launchYear }.eachCount()
                for (entry in map) {
                    add(BarEntry(entry.key.toFloat(), entry.value.toFloat()))
                }
            }
}