package com.remiboulier.rocketboard.network

import android.support.annotation.StringRes

@Suppress("DataClassPrivateConstructor")
data class NetworkState private constructor(
        val status: Status,
        @StringRes val msg: Int? = null) {

    companion object {
        val LOADED = NetworkState(Status.SUCCESS)
        val LOADING = NetworkState(Status.RUNNING)

        fun error(@StringRes msg: Int) = NetworkState(Status.FAILED, msg)
    }
}

enum class Status {
    RUNNING,
    SUCCESS,
    FAILED
}