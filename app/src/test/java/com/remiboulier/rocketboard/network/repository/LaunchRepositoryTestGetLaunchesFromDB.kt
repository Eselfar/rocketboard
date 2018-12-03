package com.remiboulier.rocketboard.network.repository

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.spy
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.remiboulier.rocketboard.network.SpaceXApi
import com.remiboulier.rocketboard.room.dao.LaunchDao
import com.remiboulier.rocketboard.room.entity.LaunchEntity
import com.remiboulier.rocketboard.testutil.RxJavaTestSetup
import io.reactivex.Single
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
class LaunchRepositoryTestGetLaunchesFromDB : RxJavaTestSetup() {

    @Mock
    lateinit var launchDao: LaunchDao

    @Mock
    lateinit var spaceXApi: SpaceXApi

    @Test
    fun getLaunchesFromDB_on_success() {
        val launches = MutableList(3) { Mockito.mock(LaunchEntity::class.java) }
        Mockito.`when`(launchDao.getAllForRocketIdAsync(anyString()))
                .thenReturn(Single.just(launches))
        val repoLaunches = spy(LaunchRepositoryImpl(spaceXApi, launchDao))

        repoLaunches.getLaunchesFromDB("", {})

        verify(repoLaunches, times(1)).onGetFromDBSuccess(anyString(), anyList(), any())
        verify(repoLaunches, times(0)).onGetFromAPISuccess(anyString(), anyList(), any())
    }

    @Test
    fun getLaunchesFromDB_on_throw_exception() {
        val repoLaunches = spy(LaunchRepositoryImpl(spaceXApi, launchDao))
        Mockito.`when`(launchDao.getAllForRocketIdAsync(anyString()))
                .thenReturn(Single.error(Mockito.mock(Exception::class.java)))

        repoLaunches.getLaunchesFromDB("", {})

        verify(repoLaunches, times(1)).onRxError(any())
    }
}