package com.remiboulier.rocketboard.screen.home

import android.util.Log

class TestItem(private val msg: String) {

    fun alive() {
        Log.d("TestItem", msg)
    }
}
