package com.remiboulier.rocketboard.network.repository

import android.arch.lifecycle.MutableLiveData
import com.remiboulier.rocketboard.mapper.LaunchDtoToEntity
import com.remiboulier.rocketboard.network.SpaceXApi
import com.remiboulier.rocketboard.network.dto.LaunchDto
import com.remiboulier.rocketboard.room.dao.LaunchDao
import com.remiboulier.rocketboard.room.entity.LaunchEntity
import com.remiboulier.rocketboard.util.getErrorMessage
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
                        { res -> onGetFromDBSuccess(rocketId, res, callback) },
                        { t -> onError(t) }))
    }

    fun getLaunchesFromAPI(rocketId: String,
                           callback: (launches: List<LaunchEntity>) -> Unit) {
        networkState.postValue(NetworkState.LOADING)
        disposables.add(spaceXApi.getLaunches()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { res -> onGetFromAPISuccess(rocketId, res, callback) },
                        { t -> onError(t) }))
    }

    fun onGetFromDBSuccess(rocketId: String,
                           entities: List<LaunchEntity>,
                           callback: (launches: List<LaunchEntity>) -> Unit) {
        if (entities.isNotEmpty()) {
            callback(entities)
            networkState.postValue(NetworkState.LOADED)
        } else
            getLaunchesFromAPI(rocketId, callback)
    }

    fun onGetFromAPISuccess(rocketId: String,
                            dtos: List<LaunchDto>,
                            callback: (launches: List<LaunchEntity>) -> Unit) {
        val mapper = Mappers.getMapper(LaunchDtoToEntity::class.java)
        val launches = filterAPIResult(rocketId, dtos).map(mapper::map)
        launchDao.saveAll(launches)
        callback(launches)
        networkState.postValue(NetworkState.LOADED)
    }

    fun filterAPIResult(rocketId: String,
                        dtos: List<LaunchDto>): List<LaunchDto> =
            dtos.filter { launch -> launch.rocket.rocketId == rocketId }

    fun onError(t: Throwable) {
        t.printStackTrace()
        networkState.postValue(NetworkState.error(getErrorMessage(t)))
    }
}