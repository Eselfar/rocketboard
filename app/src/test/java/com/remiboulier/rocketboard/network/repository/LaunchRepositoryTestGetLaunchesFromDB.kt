package com.remiboulier.rocketboard.network.repository

import com.nhaarman.mockitokotlin2.any
import com.remiboulier.rocketboard.network.SpaceXApi
import com.remiboulier.rocketboard.room.dao.LaunchDao
import com.remiboulier.rocketboard.room.entity.LaunchEntity
import com.remiboulier.rocketboard.testutil.RxJavaTestSetup
import io.reactivex.Single
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*

/**
 * Created by Remi BOULIER on 30/11/2018.
 * email: boulier.r.job@gmail.com
 */

@RunWith(org.mockito.junit.MockitoJUnitRunner::class)
class LaunchRepositoryTestGetLaunchesFromDB : RxJavaTestSetup() {

    /**
     * Workaround to prevent any(Class) from Mockito to throw an IllegalStateException
     * See https://stackoverflow.com/a/48805160/1827254
     */
    private fun <T> any(type: Class<T>): T = Mockito.any<T>(type)

    @Mock
    lateinit var launchDao: LaunchDao

    @Mock
    lateinit var spaceXApi: SpaceXApi

    @Test
    fun getLaunchesFromDB_on_success() {
        val launches = MutableList(3) { mock(LaunchEntity::class.java) }
        Mockito.`when`(launchDao.getAllForRocketIdAsync(anyString()))
                .thenReturn(Single.just(launches))
        val repo = spy(LaunchRepositoryImpl(spaceXApi, launchDao))

        repo.getLaunchesFromDB("", {})

        verify(repo, times(1))
                .onGetFromDBSuccess(anyString(), ArgumentMatchers.anyList(), any())
        verify(repo, times(0))
                .onGetFromAPISuccess(anyString(), anyList(), any())
    }

    @Test
    fun getLaunchesFromDB_on_throw_exception() {
        Mockito.`when`(launchDao.getAllForRocketIdAsync(anyString()))
                .thenReturn(Single.error(mock(Exception::class.java)))

        val repo = spy(LaunchRepositoryImpl(spaceXApi, launchDao))

        repo.getLaunchesFromDB("", {})

        verify(repo, times(1)).onRxError(any(Throwable::class.java))
    }
}