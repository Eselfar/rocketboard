package com.remiboulier.rocketboard

import android.support.v4.app.Fragment

interface MainActivityCallback {

    fun goToFragment(fragment: Fragment)

    fun updateToolbarTitle(newTitle: String)
}
