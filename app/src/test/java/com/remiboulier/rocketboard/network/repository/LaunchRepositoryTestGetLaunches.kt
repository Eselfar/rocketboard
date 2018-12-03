package com.remiboulier.rocketboard.network.repository

import com.nhaarman.mockitokotlin2.*
import com.remiboulier.rocketboard.network.SpaceXApi
import com.remiboulier.rocketboard.room.dao.LaunchDao
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock


/**
 * Created by Remi BOULIER on 30/11/2018.
 * email: boulier.r.job@gmail.com
 */
@RunWith(org.mockito.junit.MockitoJUnitRunner::class)
class LaunchRepositoryTestGetLaunches {

    @Mock
    lateinit var spaceXApi: SpaceXApi

    @Mock
    lateinit var launchDao: LaunchDao

    @Test
    fun getLaunches_call_getLaunchesFromDB() {
        val repoLaunches = spy(LaunchRepositoryImpl(spaceXApi, launchDao))
        doNothing().`when`(repoLaunches).getLaunchesFromDB(anyString(), any())

        repoLaunches.getLaunches("", false, {})

        verify(repoLaunches, times(1)).getLaunchesFromDB(anyString(), any())
        verify(repoLaunches, times(0)).getLaunchesFromAPI(anyString(), any())
    }

    @Test
    fun getLaunches_call_getLaunchesFromAPI() {
        val repoLaunches = spy(LaunchRepositoryImpl(spaceXApi, launchDao))
        doNothing().`when`(repoLaunches).getLaunchesFromAPI(anyString(), any())

        repoLaunches.getLaunches("", true, {})

        verify(repoLaunches, times(0)).getLaunchesFromDB(ArgumentMatchers.anyString(), any())
        verify(repoLaunches, times(1)).getLaunchesFromAPI(ArgumentMatchers.anyString(), any())
    }
}