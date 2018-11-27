package com.remiboulier.rocketboard.util

import com.afollestad.materialdialogs.MaterialDialog

/**
 * Created by Remi BOULIER on 27/11/2018.
 * email: boulier.r.job@gmail.com
 */

class DialogContainer {
    private var dialog: MaterialDialog? = null

    fun showDialog(md: MaterialDialog) {
        synchronized(this) {
            dialog?.dismiss()
            dialog = md
            dialog!!.show()
        }
    }

    fun dismissDialog() {
        synchronized(this) {
            dialog?.dismiss()
            dialog = null
        }
    }
}