package com.remiboulier.rocketboard.screen.launches

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.github.mikephil.charting.data.BarEntry
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.remiboulier.rocketboard.network.repository.LaunchRepository
import com.remiboulier.rocketboard.room.entity.LaunchEntity
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.*
import org.mockito.Mock
import org.mockito.Mockito

/**
 * Created by Remi BOULIER on 02/12/2018.
 * email: boulier.r.job@gmail.com
 */
@RunWith(org.mockito.junit.MockitoJUnitRunner::class)
class LaunchesFragmentViewModelTest {

    /**
     * "This rule will make sure that the writing of the value will synchronous instead of asynchronous"
     * @see [Online Post](https://proandroiddev.com/how-to-unit-test-livedata-and-lifecycle-components-8a0af41c90d9)
     */
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Mock
    lateinit var launchesRepo: LaunchRepository

    @Test
    fun onCleared() {
        val vm = LaunchesFragmentViewModelForTest(launchesRepo)
        vm.onCleared()
        Mockito.verify(launchesRepo, Mockito.times(1)).clear()
    }

    @Test
    fun loadLaunches_calls_livedata() {
        val vm = Mockito.spy(LaunchesFragmentViewModelImpl(launchesRepo))
        val results = MutableList(3) { Mockito.mock(LaunchEntity::class.java) }
        val captor = argumentCaptor<(List<LaunchEntity>) -> Unit>()

        Assert.assertNull(vm.getLaunchesLiveData().value)
        Assert.assertNull(vm.getLaunchesPerYearLiveData().value)

        vm.loadLaunches("", true)
        Mockito.verify(launchesRepo).getLaunches(anyString(), anyBoolean(), captor.capture())
        captor.lastValue.invoke(results)

        Assert.assertEquals(results, vm.getLaunchesLiveData().value)
        Assert.assertNotNull(vm.getLaunchesPerYearLiveData().value)
        verify(vm, times(1)).generateLaunchesPerYearMap(anyList())
    }

    @Test
    fun generateLaunchesPerYearMap() {
        val vm = Mockito.spy(LaunchesFragmentViewModelImpl(launchesRepo))
        val launchYears = listOf(2010, 2012, 2013, 2010, 2010, 2013)
        val launches = List(launchYears.size) { index ->
            LaunchEntity().apply { this.launchYear = launchYears[index] }
        }

        val lpys: List<BarEntry> = vm.generateLaunchesPerYearMap(launches)

        Assert.assertEquals(lpys.size, 3)
        Assert.assertEquals(lpys[0].y, 3f) // 2010
        Assert.assertEquals(lpys[1].y, 1f) // 2012
        Assert.assertEquals(lpys[2].y, 2f) // 2013
    }

}

class LaunchesFragmentViewModelForTest(launchesRepo: LaunchRepository)
    : LaunchesFragmentViewModelImpl(launchesRepo) {

    public override fun onCleared() {
        super.onCleared()
    }
}