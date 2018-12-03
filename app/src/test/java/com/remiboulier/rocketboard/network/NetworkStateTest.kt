package com.remiboulier.rocketboard.network

import org.junit.Assert
import org.junit.Test

/**
 * Created by Remi BOULIER on 03/12/2018.
 * email: boulier.r.job@gmail.com
 */
class NetworkStateTest {

    @Test
    fun netWorkState_loading_is_running() {
        val state = NetworkState.LOADING

        Assert.assertEquals(Status.RUNNING, state.status)
    }

    @Test
    fun netWorkState_loaded_is_success() {
        val state = NetworkState.LOADED

        Assert.assertEquals(Status.SUCCESS, state.status)
    }

    @Test
    fun netWorkState_error_is_failed_and_has_message() {
        val msg = 42 // String res
        val state = NetworkState.error(msg)

        Assert.assertEquals(Status.FAILED, state.status)
        Assert.assertEquals(msg, state.msg)
    }
}