package com.remiboulier.rocketboard.extension

import android.content.Context
import com.remiboulier.rocketboard.R
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by Remi BOULIER on 30/11/2018.
 * email: boulier.r.job@gmail.com
 */
@RunWith(MockitoJUnitRunner::class)
class BooleanExtensionKtTest {

    @Mock
    lateinit var context: Context

    @Test
    fun toString_returns_yes() {
        `when`(context.getString(R.string.yes)).thenReturn("Yes")
        val res = true.toString(context)

        Assert.assertEquals("Yes", res)
    }

    @Test
    fun toString_returns_no() {
        `when`(context.getString(R.string.no)).thenReturn("No")
        val res = false.toString(context)

        Assert.assertEquals("No", res)
    }
}