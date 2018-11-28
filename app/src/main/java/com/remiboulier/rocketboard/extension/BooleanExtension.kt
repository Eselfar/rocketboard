package com.remiboulier.rocketboard.extension

import android.content.Context
import com.remiboulier.rocketboard.R

/**
 * Created by Remi BOULIER on 28/11/2018.
 * email: boulier.r.job@gmail.com
 */

fun Boolean.toString(context: Context): String =
        context.getString(if (this) R.string.yes else R.string.no)
