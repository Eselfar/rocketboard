package com.remiboulier.rocketboard.util

import android.support.annotation.StringRes
import com.remiboulier.rocketboard.R
import com.remiboulier.rocketboard.network.NoNetworkException

@StringRes
fun getErrorMessage(t: Throwable): Int =
        when (t) {
            is NoNetworkException -> R.string.error_no_network
            else -> R.string.error_unknown
        }
