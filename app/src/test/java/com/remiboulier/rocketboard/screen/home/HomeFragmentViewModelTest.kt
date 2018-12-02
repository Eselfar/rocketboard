package com.remiboulier.rocketboard.screen.home

import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.remiboulier.rocketboard.network.repository.RocketRepository
import com.remiboulier.rocketboard.util.SharedPreferencesHelper
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.spy

/**
 * Created by Remi BOULIER on 30/11/2018.
 * email: boulier.r.job@gmail.com
 */
@RunWith(org.mockito.junit.MockitoJUnitRunner::class)
class HomeFragmentViewModelTest {

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

}


class HomeFragmentViewModelForTest(private val rocketRepo: RocketRepository,
                                   private val prefsHelper: SharedPreferencesHelper)
    : HomeFragmentViewModelImpl(rocketRepo, prefsHelper) {

    public override fun onCleared() {
        super.onCleared()
    }
}
