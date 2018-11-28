package com.remiboulier.rocketboard.screen.launches


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.remiboulier.rocketboard.CoreApplication
import com.remiboulier.rocketboard.R
import com.remiboulier.rocketboard.extension.displayErrorDialog
import com.remiboulier.rocketboard.extension.displayProgressDialog
import com.remiboulier.rocketboard.model.Launch
import com.remiboulier.rocketboard.network.NetworkState
import com.remiboulier.rocketboard.network.SpaceXApi
import com.remiboulier.rocketboard.network.Status
import com.remiboulier.rocketboard.util.BundleConstants
import com.remiboulier.rocketboard.util.DialogContainer
import kotlinx.android.synthetic.main.fragment_launches.*

class LaunchesFragment : Fragment() {

    private lateinit var viewModel: LaunchesFragmentViewModel

    private var adapter: LaunchAdapter? = null
    private var container: DialogContainer = DialogContainer()

    companion object {
        fun newInstance(rocketId: String, description: String): LaunchesFragment {
            return LaunchesFragment().apply {
                val bundle = Bundle()
                bundle.putString(BundleConstants.ROCKET_ID, rocketId)
                bundle.putString(BundleConstants.ROCKET_DESCRIPTION, description)
                arguments = bundle
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_launches, container, false)
    }

    override fun onStart() {
        super.onStart()

        initAdapter()

        val rocketId = arguments?.getString(BundleConstants.ROCKET_ID)
                ?: throw MissingArgumentException()
        val description = arguments?.getString(BundleConstants.ROCKET_DESCRIPTION)
                ?: throw MissingArgumentException()

        viewModel = getViewModel((activity!!.application as CoreApplication).spaceXApi, rocketId)
        viewModel.launchesLiveData.observe(this, Observer { updateUI(it!!, description) })
        viewModel.networkState.observe(this, Observer { updateState(it!!) })

        loadData()
    }

    private fun updateUI(launches: MutableList<Launch>,
                         description: String) {
        launchesDescription.text = description
        adapter?.updateList(launches)
    }

    override fun onStop() {
        super.onStop()
        adapter = null
        container.dismissDialog()
    }

    fun loadData() = viewModel.loadLaunches()

    fun initAdapter() {
        adapter = LaunchAdapter(mutableListOf())
        launchesRecycler.layoutManager = LinearLayoutManager(context!!, RecyclerView.VERTICAL, false)
        launchesRecycler.adapter = adapter
    }

    fun updateState(networkState: NetworkState) =
            when (networkState.status) {
                Status.RUNNING -> container.displayProgressDialog(context!!)
                Status.SUCCESS -> container.dismissDialog()
                Status.FAILED -> container.displayErrorDialog(context!!, networkState.msg!!)
            }

    fun getViewModel(spaceXApi: SpaceXApi, rocketId: String): LaunchesFragmentViewModel =
            ViewModelProviders.of(this, object : ViewModelProvider.Factory {
                override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                    @Suppress("UNCHECKED_CAST")
                    return LaunchesFragmentViewModel(spaceXApi, rocketId) as T
                }
            })[LaunchesFragmentViewModel::class.java]
}

class MissingArgumentException : RuntimeException("Missing required Fragment argument")
