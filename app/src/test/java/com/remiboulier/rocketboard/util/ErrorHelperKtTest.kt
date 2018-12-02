package com.remiboulier.rocketboard.util

import android.content.Context
import com.remiboulier.rocketboard.R
import com.remiboulier.rocketboard.network.NoNetworkException
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by Remi BOULIER on 30/11/2018.
 * email: boulier.r.job@gmail.com
 */
@RunWith(MockitoJUnitRunner::class)
class ErrorHelperKtTest {

    @Mock
    lateinit var context: Context

    @Test
    fun getErrorMessage_on_no_network_exception() {
        val res = getErrorMessage(NoNetworkException())

        Assert.assertEquals(R.string.error_no_network, res)
    }

    @Test
    fun getErrorMessage_on_generic_exception() {
        val res = getErrorMessage(RuntimeException())

        Assert.assertEquals(R.string.error_unknown, res)
    }
}