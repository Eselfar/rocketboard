package com.remiboulier.rocketboard.network.repository

import com.nhaarman.mockitokotlin2.any
import com.remiboulier.rocketboard.network.SpaceXApi
import com.remiboulier.rocketboard.network.dto.LaunchDto
import com.remiboulier.rocketboard.network.dto.LinksDto
import com.remiboulier.rocketboard.network.dto.RocketDetailsDto
import com.remiboulier.rocketboard.room.dao.LaunchDao
import com.remiboulier.rocketboard.testutil.RxJavaTestSetup
import io.reactivex.Observable
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*

/**
 * Created by Remi BOULIER on 30/11/2018.
 * email: boulier.r.job@gmail.com
 */

@RunWith(org.mockito.junit.MockitoJUnitRunner::class)
class LaunchRepositoryTestGetLaunchesFromAPI : RxJavaTestSetup() {

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
    fun getLaunchesFromAPI_on_success() {
        val launches = MutableList(3) { mock(LaunchDto::class.java) }
        Mockito.`when`(spaceXApi.getLaunches())
                .thenReturn(Observable.just(launches))
        val repo = spy(LaunchRepository(spaceXApi, launchDao))

        doNothing().`when`(repo).onGetFromAPISuccess(anyString(), anyList(), any())

        repo.getLaunchesFromAPI("", {})

        verify(repo, times(0))
                .onGetFromDBSuccess(anyString(), anyList(), any())
        verify(repo, times(1))
                .onGetFromAPISuccess(anyString(), anyList(), any())
    }

    @Test
    fun getLaunchesFromAPI_on_throw_exception() {
        Mockito.`when`(spaceXApi.getLaunches())
                .thenReturn(Observable.error(mock(Exception::class.java)))

        val repo = spy(LaunchRepository(spaceXApi, launchDao))

        repo.getLaunchesFromAPI("", {})

        verify(repo, times(1)).onRxError(any(Throwable::class.java))
    }

    @Test
    fun filterAPIResult_filters_correctly() {
        val rocketId = "42"
        val rocketIds = listOf(rocketId, "0", rocketId)
        val launches = List(rocketIds.size) { index -> initLaunchDtoForTest(rocketIds[index]) }

        val repo = LaunchRepository(spaceXApi, launchDao)
        val res = repo.filterAPIResult(rocketId, launches)


        assert(res.size == 2)

        val group = res.groupingBy { it.rocket.rocketId }.eachCount()
        assert(group.size == 1 && group[rocketId] == 2)
    }

    private fun initLaunchDtoForTest(rocketId: String): LaunchDto {
        return LaunchDto(0, null, false, 0,
                null, null,
                RocketDetailsDto(rocketId, ""),
                null, mock(LinksDto::class.java))
    }
}