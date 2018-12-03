package com.remiboulier.rocketboard.network.repository

import com.nhaarman.mockitokotlin2.*
import com.remiboulier.rocketboard.network.SpaceXApi
import com.remiboulier.rocketboard.network.dto.LaunchDto
import com.remiboulier.rocketboard.room.dao.LaunchDao
import com.remiboulier.rocketboard.room.entity.LaunchEntity
import com.remiboulier.rocketboard.testutil.RxJavaTestSetup
import io.reactivex.Observable
import junit.framework.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyList
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito


/**
 * Created by Remi BOULIER on 30/11/2018.
 * email: boulier.r.job@gmail.com
 */

@RunWith(org.mockito.junit.MockitoJUnitRunner::class)
class LaunchRepositoryTestGetLaunchesFromAPI : RxJavaTestSetup() {

    @Mock
    lateinit var launchDao: LaunchDao

    @Mock
    lateinit var spaceXApi: SpaceXApi

    @Test
    fun getLaunchesFromAPI_on_success() {
        val repoLaunches = spy(LaunchRepositoryImpl(spaceXApi, launchDao))
        val launches = MutableList(3) { Mockito.mock(LaunchDto::class.java) }
        Mockito.`when`(spaceXApi.getLaunches())
                .thenReturn(Observable.just(launches))
        doNothing().`when`(repoLaunches).onGetFromAPISuccess(anyString(), anyList(), any())

        repoLaunches.getLaunchesFromAPI("", {})

        verify(repoLaunches, times(0)).onGetFromDBSuccess(anyString(), anyList(), any())
        verify(repoLaunches, times(1)).onGetFromAPISuccess(anyString(), anyList(), any())
    }

    @Test
    fun getLaunchesFromAPI_on_throw_exception() {
        val repoLaunches = spy(LaunchRepositoryImpl(spaceXApi, launchDao))
        Mockito.`when`(spaceXApi.getLaunches())
                .thenReturn(Observable.error(Mockito.mock(Exception::class.java)))

        repoLaunches.getLaunchesFromAPI("", {})

        verify(repoLaunches, times(1)).onRxError(any())
    }

    @Test
    fun filterEntities_filters_correctly() {
        val repoLaunches = LaunchRepositoryImpl(spaceXApi, launchDao)
        val rocketId = "42"
        val rocketIds = listOf(rocketId, "0", rocketId)
        val launches = List(rocketIds.size) { index ->
            LaunchEntity().apply { this.rocketId = rocketIds[index] }
        }
        val res = repoLaunches.filterEntities(rocketId, launches)

        Assert.assertTrue(res.size == 2)
        val group = res.groupingBy { it.rocketId }.eachCount()
        Assert.assertTrue(group.size == 1 && group[rocketId] == 2)
    }
}