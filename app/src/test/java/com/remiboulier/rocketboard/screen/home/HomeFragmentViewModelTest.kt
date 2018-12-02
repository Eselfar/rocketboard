package com.remiboulier.rocketboard.screen.home

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.remiboulier.rocketboard.network.repository.RocketRepository
import com.remiboulier.rocketboard.room.entity.RocketEntity
import com.remiboulier.rocketboard.util.SharedPreferencesHelper
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*


/**
 * Created by Remi BOULIER on 30/11/2018.
 * email: boulier.r.job@gmail.com
 */
@RunWith(org.mockito.junit.MockitoJUnitRunner::class)
class HomeFragmentViewModelTest {

    /**
     * Workaround to prevent any(Class) from Mockito to throw an IllegalStateException
     * See https://stackoverflow.com/a/48805160/1827254
     */
    private fun <T> any(type: Class<T>): T = Mockito.any<T>(type)

    @Mock
    lateinit var rocketRepo: RocketRepository

    @Mock
    lateinit var prefsHelper: SharedPreferencesHelper

    @Test
    fun onCleared() {
        val vm = HomeFragmentViewModelForTest(rocketRepo, prefsHelper)
        vm.onCleared()
        verify(rocketRepo, times(1)).clear()
    }

    @Test
    fun loadRocket_call_updateLiveData() {
        val vm = spy(HomeFragmentViewModelImpl(rocketRepo, prefsHelper))
        vm.rockets = MutableList(3) { Mockito.mock(RocketEntity::class.java) }
        doNothing().`when`(vm).updateLiveData(anyList(), anyBoolean())

        vm.loadRockets(false)

        verify(vm, times(1)).updateLiveData(anyList(), anyBoolean())
        verify(vm, times(0)).getDataFromRepo(anyBoolean())
    }

    @Test
    fun loadRocket_call_getDataFromRepo_when_forceRefresh_and_rocket_not_empty() {
        val vm = spy(HomeFragmentViewModelImpl(rocketRepo, prefsHelper))
        vm.rockets = MutableList(3) { Mockito.mock(RocketEntity::class.java) }
        doNothing().`when`(vm).getDataFromRepo(anyBoolean())

        vm.loadRockets(true)

        verify(vm, times(0)).updateLiveData(anyList(), anyBoolean())
        verify(vm, times(1)).getDataFromRepo(anyBoolean())
    }


    @Test
    fun loadRocket_call_getDataFromRepo_when_forceRefresh_and_rocket_empty() {
        val vm = spy(HomeFragmentViewModelImpl(rocketRepo, prefsHelper))
        vm.rockets = listOf()
        doNothing().`when`(vm).getDataFromRepo(anyBoolean())

        vm.loadRockets(true)

        verify(vm, times(0)).updateLiveData(anyList(), anyBoolean())
        verify(vm, times(1)).getDataFromRepo(anyBoolean())
    }

    @Test
    fun isFirstTime_true() {
        Mockito.`when`(prefsHelper.getIsFirstTime()).thenReturn(true)
        val vm = spy(HomeFragmentViewModelImpl(rocketRepo, prefsHelper))

        Assert.assertTrue(vm.isFirstTime())
        verify(prefsHelper, times(1)).storeIsFirstTime(ArgumentMatchers.anyBoolean())
    }

    @Test
    fun isFirstTime_false() {
        Mockito.`when`(prefsHelper.getIsFirstTime()).thenReturn(false)
        val vm = spy(HomeFragmentViewModelImpl(rocketRepo, prefsHelper))

        Assert.assertFalse(vm.isFirstTime())
        verify(prefsHelper, times(0)).storeIsFirstTime(ArgumentMatchers.anyBoolean())
    }

    @Test
    fun updateActiveOnly_when_change_to_true() {
        val vm = spy(HomeFragmentViewModelImpl(rocketRepo, prefsHelper))
        doNothing().`when`(vm).updateLiveData(anyList(), anyBoolean())
        vm.activeOnly = false

        vm.updateActiveOnly(true)

        Assert.assertTrue(vm.activeOnly)
        verify(vm, times(1)).updateLiveData(anyList(), anyBoolean())
    }

    @Test
    fun updateActiveOnly_when_change_to_false() {
        val vm = spy(HomeFragmentViewModelImpl(rocketRepo, prefsHelper))
        doNothing().`when`(vm).updateLiveData(anyList(), anyBoolean())
        vm.activeOnly = true

        vm.updateActiveOnly(false)

        Assert.assertFalse(vm.activeOnly)
        verify(vm, times(1)).updateLiveData(anyList(), anyBoolean())
    }

    @Test
    fun updateActiveOnly_when_not_changing_from_true() {
        val vm = spy(HomeFragmentViewModelImpl(rocketRepo, prefsHelper))
        vm.activeOnly = true

        vm.updateActiveOnly(true)

        Assert.assertTrue(vm.activeOnly)
        verify(vm, times(0)).updateLiveData(anyList(), anyBoolean())
    }

    @Test
    fun updateActiveOnly_when_not_changing_from_false() {
        val vm = spy(HomeFragmentViewModelImpl(rocketRepo, prefsHelper))
        vm.activeOnly = false

        vm.updateActiveOnly(false)

        Assert.assertFalse(vm.activeOnly)
        verify(vm, times(0)).updateLiveData(anyList(), anyBoolean())
    }


    @Test
    fun getDataFromRepo_updateLiveData_and_local() {
        val vm = spy(HomeFragmentViewModelImpl(rocketRepo, prefsHelper))
        vm.rockets = listOf()
        val results = MutableList(3) { Mockito.mock(RocketEntity::class.java) }
        val captor = argumentCaptor<(List<RocketEntity>) -> Unit>()

        vm.getDataFromRepo(true)
        verify(rocketRepo).getRockets(anyBoolean(), captor.capture())
        captor.lastValue.invoke(results)

        Assert.assertEquals(results, vm.rockets)
        Mockito.verify(vm, Mockito.times(1)).updateLiveData(anyList(), anyBoolean())
    }


    @Test
    fun filterResults_no_filter() {
        val vm = spy(HomeFragmentViewModelImpl(rocketRepo, prefsHelper))
        val rocketActive = listOf(true, false, true)
        val rockets = List(rocketActive.size) { index ->
            RocketEntity().apply { this.active = rocketActive[index] }
        }

        val res = vm.filterResults(rockets, false)

        Assert.assertEquals(rockets, res)
    }


    @Test
    fun filterResults_active_only() {
        val vm = spy(HomeFragmentViewModelImpl(rocketRepo, prefsHelper))
        val rocketActive = listOf(true, false, true)
        val rockets = List(rocketActive.size) { index ->
            RocketEntity().apply { this.active = rocketActive[index] }
        }

        val res = vm.filterResults(rockets, true)

        val group = res.groupingBy { it.active }.eachCount()
        Assert.assertTrue(group.size == 1 && group[true] == 2)
    }


    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()


    @Test
    fun updateLiveData_update() {
        val vm = spy(HomeFragmentViewModelImpl(rocketRepo, prefsHelper))
        val rockets = MutableList(3) { Mockito.mock(RocketEntity::class.java) }
        val liveData = vm.getRocketsLiveData()

        Assert.assertNull(liveData.value)

        vm.updateLiveData(rockets, false)

        Assert.assertNotNull(liveData.value)
        Mockito.verify(vm, Mockito.times(1)).filterResults(anyList(), anyBoolean())
    }
}

class HomeFragmentViewModelForTest(rocketRepo: RocketRepository,
                                   prefsHelper: SharedPreferencesHelper)
    : HomeFragmentViewModelImpl(rocketRepo, prefsHelper) {

    public override fun onCleared() {
        super.onCleared()
    }
}
