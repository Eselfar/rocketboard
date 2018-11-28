package com.remiboulier.rocketboard.extension

import org.junit.Assert
import org.junit.Test
import java.util.*

/**
 * Created by Remi BOULIER on 28/11/2018.
 * email: boulier.r.job@gmail.com
 */
class IntExtensionKtTest {

    @Test
    fun toReadableDate() {
        val calendar = Calendar.getInstance()
        calendar.set(2018, Calendar.DECEMBER, 14, 13, 24, 30)

        val expected = "14 Dec 2018 13:24"
        val res = (calendar.timeInMillis / 1_000).toInt().toReadableDate()

        Assert.assertEquals(expected, res)
    }
}