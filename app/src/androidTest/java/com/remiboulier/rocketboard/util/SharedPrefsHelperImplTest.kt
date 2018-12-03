package com.remiboulier.rocketboard.util

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by Remi BOULIER on 03/12/2018.
 * email: boulier.r.job@gmail.com
 */

@RunWith(AndroidJUnit4::class)
class SharedPrefsHelperImplTest {

    lateinit var mPrefsHelper: SharedPrefsHelper

    @Before
    fun initTest() {
        val context = InstrumentationRegistry.getContext()
        mPrefsHelper = SharedPrefsHelperImpl(context)
    }

    @Test
    fun storeIsFirstTime() {
        Assert.assertTrue(mPrefsHelper.getIsFirstTime())

        mPrefsHelper.storeIsFirstTime(false)

        Assert.assertFalse(mPrefsHelper.getIsFirstTime())
    }
}