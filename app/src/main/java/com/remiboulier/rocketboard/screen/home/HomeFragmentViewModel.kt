package com.remiboulier.rocketboard.screen.home

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.remiboulier.rocketboard.model.Rocket
import com.remiboulier.rocketboard.network.NetworkState
import com.remiboulier.rocketboard.network.SpaceXApi
import com.remiboulier.rocketboard.util.SharedPreferencesHelper
import com.remiboulier.rocketboard.util.getErrorMessage
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by Remi BOULIER on 27/11/2018.
 * email: boulier.r.job@gmail.com
 */

class HomeFragmentViewModel(private val spaceXApi: SpaceXApi,
                            private val prefsHelper: SharedPreferencesHelper) : ViewModel() {

    val rocketsLiveData = MutableLiveData<MutableList<Rocket>>()
    val networkState = MutableLiveData<NetworkState>()

    private var activeOnly = false

    private val disposables = CompositeDisposable()
    private val rockets = mutableListOf<Rocket>()

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }

    fun loadRockets() {
        networkState.postValue(NetworkState.LOADING)

        disposables.add(spaceXApi.getRockets()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { res ->
                            rockets.clear()
                            rockets.addAll(res)
                            rocketsLiveData.postValue(filterResults(rockets, activeOnly))
                            networkState.postValue(NetworkState.LOADED)
                        },
                        { t ->
                            t.printStackTrace()
                            networkState.postValue(NetworkState.error(getErrorMessage(t)))
                        }))
    }

    fun isFirstTime(): Boolean = prefsHelper.getIsFirstTime().also {
        if (it) prefsHelper.storeIsFirstTime(false)
    }

    fun updateActiveOnly(activeOnly: Boolean) {
        this.activeOnly = activeOnly
        rocketsLiveData.postValue(filterResults(rockets, activeOnly))
    }

    fun filterResults(rockets: MutableList<Rocket>, active: Boolean): MutableList<Rocket> =
            if (active) rockets.filter { rocket -> rocket.active }.toMutableList()
            else rockets
}