package com.remiboulier.rocketboard.network.repository

import android.arch.lifecycle.MutableLiveData
import com.remiboulier.rocketboard.mapper.LaunchDtoToEntity
import com.remiboulier.rocketboard.network.SpaceXApi
import com.remiboulier.rocketboard.room.dao.LaunchDao
import com.remiboulier.rocketboard.room.entity.LaunchEntity
import com.remiboulier.rocketboard.util.getErrorMessage
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.mapstruct.factory.Mappers

/**
 * Created by Remi BOULIER on 30/11/2018.
 * email: boulier.r.job@gmail.com
 */
class LaunchRepository(private val spaceXApi: SpaceXApi,
                       private val launchDao: LaunchDao) {

    private val disposables = CompositeDisposable()
    val networkState = MutableLiveData<NetworkState>()

    fun clear() {
        disposables.clear()
    }

    fun getLaunches(rocketId: String,
                    forceRefresh: Boolean,
                    callback: (launches: List<LaunchEntity>) -> Unit) {
        if (!forceRefresh) getLaunchesFromDB(rocketId, callback)
        else getLaunchesFromAPI(rocketId, callback)
    }

    fun getLaunchesFromDB(rocketId: String,
                          callback: (launches: List<LaunchEntity>) -> Unit) {
        networkState.postValue(NetworkState.LOADING)
        disposables.add(launchDao.getAllForRocketIdAsync(rocketId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { res ->
                            if (res.isNotEmpty()) {
                                callback(res)
                                networkState.postValue(NetworkState.LOADED)
                            } else
                                getLaunchesFromAPI(rocketId, callback)
                        },
                        { t ->
                            t.printStackTrace()
                            networkState.postValue(NetworkState.error(getErrorMessage(t)))
                        }))
    }

    fun getLaunchesFromAPI(rocketId: String,
                           callback: (launches: List<LaunchEntity>) -> Unit) {
        networkState.postValue(NetworkState.LOADING)
        disposables.add(spaceXApi.getLaunches()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap { res -> Observable.fromIterable(res) }
                .filter { launch -> launch.rocket.rocketId == rocketId }
                .toList()
                .subscribe(
                        { res ->
                            val mapper = Mappers.getMapper(LaunchDtoToEntity::class.java)
                            val launches = res.map(mapper::map)
                            launchDao.saveAll(launches)
                            callback(launches)
                            networkState.postValue(NetworkState.LOADED)
                        },
                        { t ->
                            t.printStackTrace()
                            networkState.postValue(NetworkState.error(getErrorMessage(t)))
                        }))
    }
}