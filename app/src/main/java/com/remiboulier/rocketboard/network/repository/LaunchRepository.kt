package com.remiboulier.rocketboard.network.repository

import android.arch.lifecycle.MutableLiveData
import com.remiboulier.rocketboard.mapper.LaunchDtoToEntity
import com.remiboulier.rocketboard.network.NetworkState
import com.remiboulier.rocketboard.network.SpaceXApi
import com.remiboulier.rocketboard.network.dto.LaunchDto
import com.remiboulier.rocketboard.room.dao.LaunchDao
import com.remiboulier.rocketboard.room.entity.LaunchEntity
import com.remiboulier.rocketboard.util.getErrorMessage
import io.reactivex.Completable
import io.reactivex.CompletableObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
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
        networkState.postValue(NetworkState.LOADING)
        if (!forceRefresh) getLaunchesFromDB(rocketId, callback)
        else getLaunchesFromAPI(rocketId, callback)
    }

    fun getLaunchesFromDB(rocketId: String,
                          callback: (launches: List<LaunchEntity>) -> Unit) {
        disposables.add(launchDao.getAllForRocketIdAsync(rocketId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { res -> onGetFromDBSuccess(rocketId, res, callback) },
                        { t -> onRxError(t) }))
    }

    fun getLaunchesFromAPI(rocketId: String,
                           callback: (launches: List<LaunchEntity>) -> Unit) {
        disposables.add(spaceXApi.getLaunches()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { res -> onGetFromAPISuccess(rocketId, res, callback) },
                        { t -> onRxError(t) }))
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
        val launches = dtos.map(mapper::map)
        saveInDatabase(launches) { callback(filterEntities(rocketId, it)) }
    }

    fun filterEntities(rocketId: String,
                       dtos: List<LaunchEntity>): List<LaunchEntity> =
            dtos.filter { launch -> launch.rocketId == rocketId }

    fun onRxError(t: Throwable) {
        t.printStackTrace()
        networkState.postValue(NetworkState.error(getErrorMessage(t)))
    }

    private fun saveInDatabase(launches: List<LaunchEntity>,
                               callback: (rockets: List<LaunchEntity>) -> Unit) {
        Completable.fromAction { launchDao.saveAll(launches) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : CompletableObserver {
                    override fun onSubscribe(d: Disposable) {}

                    override fun onComplete() {
                        callback(launches)
                        networkState.postValue(NetworkState.LOADED)
                    }

                    override fun onError(t: Throwable) = onRxError(t)
                })
    }
}