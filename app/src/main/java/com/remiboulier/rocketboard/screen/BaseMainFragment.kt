package com.remiboulier.rocketboard.screen

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import com.remiboulier.rocketboard.MainActivityCallback

/**
 * Created by Remi BOULIER on 29/11/2018.
 * email: boulier.r.job@gmail.com
 */

abstract class BaseMainFragment : Fragment() {

    lateinit var barTitle: String

    protected var activityCallback: MainActivityCallback? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            activityCallback = activity as MainActivityCallback?
        } catch (e: ClassCastException) {
            throw ClassCastException(this.javaClass.simpleName + " must implement MainActivityCallback")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activityCallback!!.updateToolbarTitle(barTitle)
    }
}