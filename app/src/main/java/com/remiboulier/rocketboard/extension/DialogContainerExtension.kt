package com.remiboulier.rocketboard.extension

import android.content.Context
import android.support.annotation.StringRes
import com.afollestad.materialdialogs.MaterialDialog
import com.remiboulier.rocketboard.R
import com.remiboulier.rocketboard.util.DialogContainer

/**
 * Created by Remi BOULIER on 27/11/2018.
 * email: boulier.r.job@gmail.com
 */

fun DialogContainer.displayWelcomeDialog(context: Context,
                                         onPositive: () -> Unit) =
        showDialog(MaterialDialog.Builder(context)
                .title(R.string.app_name)
                .content(R.string.welcome_message)
                .cancelable(false)
                .positiveText(R.string.got_it)
                .onPositive { _, _ -> onPositive() }
                .build())


fun DialogContainer.displayProgressDialog(context: Context) =
        showDialog(MaterialDialog.Builder(context)
                .title(R.string.app_name)
                .content(R.string.please_wait)
                .cancelable(false)
                .progress(true, 0)
                .build())

fun DialogContainer.displayErrorDialog(context: Context,
                                       @StringRes msg: Int) =
        showDialog(MaterialDialog.Builder(context)
                .title(R.string.app_name)
                .cancelable(true)
                .content(context.getString(msg))
                .positiveText(R.string.got_it)
                .build())