package com.remiboulier.rocketboard.extension

import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Remi BOULIER on 18/11/2018.
 * email: boulier.r.job@gmail.com
 */

fun Int.toReadableDate(): String {
    val df = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.UK)

    return df.format(Date(this * 1_000L))
}