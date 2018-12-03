package com.remiboulier.rocketboard.util

import android.content.Context
import android.content.Context.MODE_PRIVATE

/**
 * Created by Remi BOULIER on 27/11/2018.
 * email: boulier.r.job@gmail.com
 */

class SharedPrefsHelperImpl(private val context: Context) : SharedPrefsHelper {

    override fun storeIsFirstTime(isFirstTime: Boolean) =
            context.getSharedPreferences(SharedPrefs.FILE_GLOBAL, MODE_PRIVATE)
                    .edit()
                    .putBoolean(SharedPrefs.KEY_IS_FIRST_TIME, isFirstTime)
                    .apply()

    override fun getIsFirstTime() =
            context.getSharedPreferences(SharedPrefs.FILE_GLOBAL, MODE_PRIVATE)
                    .getBoolean(SharedPrefs.KEY_IS_FIRST_TIME, true)
}