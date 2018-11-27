package com.remiboulier.rocketboard

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.remiboulier.rocketboard.model.Rocket
import com.remiboulier.rocketboard.network.NetworkState
import com.remiboulier.rocketboard.network.SpaceXApi
import com.remiboulier.rocketboard.util.SharedPreferencesHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MainActivityViewModel(private val spaceXApi: SpaceXApi,
                            private val prefsHelper: SharedPreferencesHelper) : ViewModel() {

    val rocketsLiveData = MutableLiveData<MutableList<Rocket>>()
    val networkState = MutableLiveData<NetworkState>()

    private val disposables = CompositeDisposable()

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
                            rocketsLiveData.postValue(res)
                            networkState.postValue(NetworkState.LOADED)
                        },
                        { t ->
                            t.printStackTrace()
                            networkState.postValue(NetworkState.error(null/*TODO*/))
                        }))
    }

    fun isFirstTime(): Boolean {
        val isFirstTime = prefsHelper.getIsFirstTime()
        if (isFirstTime) {
            prefsHelper.storeIsFirstTime(false)
        }

        return isFirstTime
    }
}
