package com.remiboulier.rocketboard.network

import android.support.annotation.StringRes

enum class Status {
    RUNNING,
    SUCCESS,
    FAILED
}

@Suppress("DataClassPrivateConstructor")
data class NetworkState private constructor(
        val status: Status,
        @StringRes val msg: Int? = null) {

    companion object {
        val LOADED = NetworkState(Status.SUCCESS)
        val LOADING = NetworkState(Status.RUNNING)

        fun error(msg: Int) = NetworkState(Status.FAILED, msg)
    }
}