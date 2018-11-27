package com.remiboulier.rocketboard

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.remiboulier.rocketboard.model.Rocket
import com.remiboulier.rocketboard.network.SpaceXApi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MainActivityViewModel(private val spaceXApi: SpaceXApi) : ViewModel() {

    val rocketsLiveData = MutableLiveData<MutableList<Rocket>>()

    fun loadRockets() {
        spaceXApi.getRockets()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { res -> rocketsLiveData.postValue(res) },
                        { t -> t.printStackTrace() })
    }
}
