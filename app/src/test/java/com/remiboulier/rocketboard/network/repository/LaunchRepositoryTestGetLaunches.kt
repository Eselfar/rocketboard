package com.remiboulier.rocketboard.network.repository

import com.nhaarman.mockitokotlin2.argumentCaptor
import com.remiboulier.rocketboard.network.SpaceXApi
import com.remiboulier.rocketboard.room.dao.LaunchDao
import com.remiboulier.rocketboard.room.entity.LaunchEntity
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.*


/**
 * Created by Remi BOULIER on 30/11/2018.
 * email: boulier.r.job@gmail.com
 */

class LaunchRepositoryTestGetLaunches {

    @Test
    fun getLaunches_call_getLaunchesFromDB() {

        val repoSpy = spy(LaunchRepositoryImpl(
                mock(SpaceXApi::class.java),
                mock(LaunchDao::class.java)))

        val captor = argumentCaptor<(List<LaunchEntity>) -> Unit>()

        doNothing().`when`(repoSpy).getLaunchesFromDB(anyString(), captor.capture())

        repoSpy.getLaunches("", false, {})

        verify(repoSpy, times(1))
                .getLaunchesFromDB(ArgumentMatchers.anyString(), captor.capture())

        verify(repoSpy, times(0))
                .getLaunchesFromAPI(ArgumentMatchers.anyString(), captor.capture())
    }

    @Test
    fun getLaunches_call_getLaunchesFromAPI() {

        val repoSpy = spy(LaunchRepositoryImpl(
                mock(SpaceXApi::class.java),
                mock(LaunchDao::class.java)))

        val captor = argumentCaptor<(List<LaunchEntity>) -> Unit>()

        doNothing().`when`(repoSpy).getLaunchesFromAPI(anyString(), captor.capture())

        repoSpy.getLaunches("", true, {})

        verify(repoSpy, times(0))
                .getLaunchesFromDB(ArgumentMatchers.anyString(), captor.capture())

        verify(repoSpy, times(1))
                .getLaunchesFromAPI(ArgumentMatchers.anyString(), captor.capture())
    }
}