package com.remiboulier.rocketboard.network.repository

import android.arch.lifecycle.MutableLiveData
import com.remiboulier.rocketboard.mapper.RocketDtoToEntityMapper
import com.remiboulier.rocketboard.network.NetworkState
import com.remiboulier.rocketboard.network.SpaceXApi
import com.remiboulier.rocketboard.network.dto.RocketDto
import com.remiboulier.rocketboard.room.dao.RocketDao
import com.remiboulier.rocketboard.room.entity.RocketEntity
import com.remiboulier.rocketboard.util.getErrorMessage
import io.reactivex.Completable
import io.reactivex.CompletableObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.mapstruct.factory.Mappers


/**
 * Created by Remi BOULIER on 29/11/2018.
 * email: boulier.r.job@gmail.com
 */
class RocketRepository(private val spaceXApi: SpaceXApi,
                       private val rocketDao: RocketDao) {

    private val disposables = CompositeDisposable()
    val networkState = MutableLiveData<NetworkState>()

    fun clear() {
        disposables.clear()
    }

    fun getRockets(forceRefresh: Boolean,
                   callback: (rockets: List<RocketEntity>) -> Unit) {
        if (!forceRefresh) getRocketsFromDB(callback)
        else getRocketsFromAPI(callback)
    }

    fun getRocketsFromDB(callback: (rockets: List<RocketEntity>) -> Unit) {
        networkState.postValue(NetworkState.LOADING)
        disposables.add(rocketDao.getAllAsync()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { res -> onGetFromDBSuccess(res, callback) },
                        { t -> onRxError(t) }))
    }

    fun getRocketsFromAPI(callback: (rockets: List<RocketEntity>) -> Unit) {
        networkState.postValue(NetworkState.LOADING)
        disposables.add(spaceXApi.getRockets()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { res -> onGetFromAPISuccess(res, callback) },
                        { t -> onRxError(t) }))
    }

    fun onGetFromDBSuccess(res: List<RocketEntity>,
                           callback: (rockets: List<RocketEntity>) -> Unit) {
        if (res.isNotEmpty()) {
            callback(res)
            networkState.postValue(NetworkState.LOADED)
        } else
            getRocketsFromAPI(callback)
    }

    fun onGetFromAPISuccess(rocketDtos: List<RocketDto>,
                            callback: (rockets: List<RocketEntity>) -> Unit) {
        val mapper = Mappers.getMapper(RocketDtoToEntityMapper::class.java)
        val rocketEntities = rocketDtos.map(mapper::map)
        saveInDatabase(rocketEntities) {
            callback(rocketEntities)
            networkState.postValue(NetworkState.LOADED)
        }
    }

    fun onRxError(t: Throwable) {
        t.printStackTrace()
        networkState.postValue(NetworkState.error(getErrorMessage(t)))
    }

    private fun saveInDatabase(rockets: List<RocketEntity>,
                               onSaved: () -> Unit) {
        Completable.fromAction { rocketDao.saveAll(rockets) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : CompletableObserver {
                    override fun onSubscribe(d: Disposable) {}

                    override fun onComplete() = onSaved()

                    override fun onError(t: Throwable) = onRxError(t)
                })
    }
}
