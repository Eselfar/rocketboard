package com.remiboulier.rocketboard.network.repository

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.*
import com.remiboulier.rocketboard.network.NetworkState
import com.remiboulier.rocketboard.network.SpaceXApi
import com.remiboulier.rocketboard.network.dto.RocketDto
import com.remiboulier.rocketboard.room.dao.RocketDao
import com.remiboulier.rocketboard.room.entity.RocketEntity
import com.remiboulier.rocketboard.testutil.RxJavaTestSetup
import io.reactivex.Observable
import io.reactivex.Single
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyList
import org.mockito.Mock
import org.mockito.Mockito

/**
 * Created by Remi BOULIER on 02/12/2018.
 * email: boulier.r.job@gmail.com
 */
@RunWith(org.mockito.junit.MockitoJUnitRunner::class)
class RocketRepositoryImplTest : RxJavaTestSetup() {

    /**
     * "This rule will make sure that the writing of the value will synchronous instead of asynchronous"
     * @see [Online Post](https://proandroiddev.com/how-to-unit-test-livedata-and-lifecycle-components-8a0af41c90d9)
     */
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Mock
    lateinit var spaceXApi: SpaceXApi

    @Mock
    lateinit var rocketDao: RocketDao

    lateinit var rocketRepo: RocketRepositoryImpl

    @Before
    fun initTest() {
        rocketRepo = spy(RocketRepositoryImpl(spaceXApi, rocketDao))
    }

    @Test
    fun getRockets_update_networkStateLiveData() {
        val liveData = rocketRepo.getNetworkStateLiveData()
        doNothing().`when`(rocketRepo).getRocketsFromDB(any())

        Assert.assertNull(liveData.value)

        rocketRepo.getRockets(false, {})

        Assert.assertEquals(NetworkState.LOADING, liveData.value)
    }

    @Test
    fun getRockets_calls_getLaunchesFromDB() {
        doNothing().`when`(rocketRepo).getRocketsFromDB(any())

        rocketRepo.getRockets(false, {})

        verify(rocketRepo, times(1)).getRocketsFromDB(any())
        verify(rocketRepo, times(0)).getRocketsFromAPI(any())
    }

    @Test
    fun getRockets_calls_getLaunchesFromAPI() {
        doNothing().`when`(rocketRepo).getRocketsFromAPI(any())

        rocketRepo.getRockets(true, {})

        verify(rocketRepo, times(0)).getRocketsFromDB(any())
        verify(rocketRepo, times(1)).getRocketsFromAPI(any())
    }

    @Test
    fun getRocketsFromDB_onSuccess() {
        val rockets = listOf<RocketEntity>()
        doNothing().`when`(rocketRepo).onGetFromDBSuccess(anyList(), any())
        val listCaptor = argumentCaptor<List<RocketEntity>>()
        Mockito.`when`(rocketDao.getAllAsync())
                .thenReturn(Single.just(rockets))

        rocketRepo.getRocketsFromDB { }

        verify(rocketRepo, times(1)).onGetFromDBSuccess(listCaptor.capture(), any())
        Assert.assertEquals(rockets, listCaptor.lastValue)
    }

    @Test
    fun getRocketFromDB_on_throw_exception() {
        Mockito.`when`(rocketDao.getAllAsync())
                .thenReturn(Single.error(Mockito.mock(Exception::class.java)))
        doNothing().`when`(rocketRepo).onRxError(any())

        rocketRepo.getRocketsFromDB { }

        verify(rocketRepo, times(1)).onRxError(any())
    }

    @Test
    fun getRocketFromAPI_on_success() {
        val rocketsDto = listOf<RocketDto>()
        doNothing().`when`(rocketRepo).onGetFromAPISuccess(anyList(), any())
        val listCaptor = argumentCaptor<List<RocketDto>>()
        Mockito.`when`(spaceXApi.getRockets())
                .thenReturn(Observable.just(rocketsDto))

        rocketRepo.getRocketsFromAPI { }

        verify(rocketRepo, times(1)).onGetFromAPISuccess(listCaptor.capture(), any())
        Assert.assertEquals(rocketsDto, listCaptor.lastValue)
    }

    @Test
    fun getRocketFromAPI_on_throw_exception() {
        Mockito.`when`(spaceXApi.getRockets())
                .thenReturn(Observable.error(Mockito.mock(Exception::class.java)))
        doNothing().`when`(rocketRepo).onRxError(any())

        rocketRepo.getRocketsFromAPI { }

        verify(rocketRepo, times(1)).onRxError(any())
    }


}


