package com.remiboulier.rocketboard.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import okhttp3.Interceptor
import okhttp3.Response

class ConnectivityInterceptor(private val context: Context) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        if (isConnected()) {
            return chain.proceed(chain.request())
        } else {
            throw NoNetworkException()
        }
    }

    /**
     * Test if the user has connection data
     * @return True if connected
     *
     * @see [Android documentation] (https://developer.android.com/training/monitoring-device-state/connectivity-monitoring#kotlin)
     */
    private fun isConnected(): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo

        return activeNetwork?.isConnectedOrConnecting == true
    }
}

class NoNetworkException : RuntimeException("Please check your data connection")