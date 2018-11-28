package com.remiboulier.rocketboard.screen.details


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.remiboulier.rocketboard.R
import com.remiboulier.rocketboard.util.BundleConstants

/**
 * A simple [Fragment] subclass.
 *
 */
class DetailsFragment : Fragment() {

    companion object {
        fun newInstance(rocketId: String): DetailsFragment {
            return DetailsFragment().apply {
                val bundle = Bundle()
                bundle.putString(BundleConstants.ROCKET_ID, rocketId)
                arguments = bundle
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_details, container, false)
    }
}
