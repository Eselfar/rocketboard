package com.remiboulier.rocketboard.network.repository

import android.arch.lifecycle.MutableLiveData
import com.remiboulier.rocketboard.mapper.RocketDtoToEntityMapper
import com.remiboulier.rocketboard.network.SpaceXApi
import com.remiboulier.rocketboard.room.dao.RocketDao
import com.remiboulier.rocketboard.room.entity.RocketEntity
import com.remiboulier.rocketboard.util.getErrorMessage
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
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

    fun getRockets(forceRefresh: Boolean,
                   callback: (rockets: List<RocketEntity>) -> Unit) {
        if (!forceRefresh) getRocketsFromDB(callback)
        else getRocketsFromAPI(callback)
    }

    fun getRocketsFromDB(callback: (rockets: List<RocketEntity>) -> Unit) {
        networkState.postValue(NetworkState.LOADING)
        rocketDao.getAllAsync()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { res ->
                            if (res.isNotEmpty()) {
                                callback(res)
                                networkState.postValue(NetworkState.LOADED)
                            } else
                                getRocketsFromAPI(callback)
                        },
                        { t ->
                            t.printStackTrace()
                            networkState.postValue(NetworkState.error(getErrorMessage(t)))
                        })
    }

    fun clear() {
        disposables.clear()
    }

    fun getRocketsFromAPI(callback: (rockets: List<RocketEntity>) -> Unit) {
        networkState.postValue(NetworkState.LOADING)
        disposables.add(spaceXApi.getRockets()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { res ->
                            val mapper = Mappers.getMapper(RocketDtoToEntityMapper::class.java)
                            val rockets = res.map(mapper::map)
                            rocketDao.saveAll(rockets)
                            callback(rockets)
                            networkState.postValue(NetworkState.LOADED)
                        },
                        { t ->
                            t.printStackTrace()
                            networkState.postValue(NetworkState.error(getErrorMessage(t)))
                        }))
    }
}
