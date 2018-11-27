package com.remiboulier.rocketboard.util

interface SharedPreferencesHelper {

    fun storeIsFirstTime(isFirstTime: Boolean)

    fun getIsFirstTime(): Boolean
}
