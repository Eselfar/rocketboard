package com.remiboulier.rocketboard.util

interface SharedPrefsHelper {

    fun storeIsFirstTime(isFirstTime: Boolean)

    fun getIsFirstTime(): Boolean
}
