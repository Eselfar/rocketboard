package com.remiboulier.rocketboard.screen.home


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.remiboulier.rocketboard.CoreApplication
import com.remiboulier.rocketboard.R
import com.remiboulier.rocketboard.extension.displayErrorDialog
import com.remiboulier.rocketboard.extension.displayProgressDialog
import com.remiboulier.rocketboard.extension.displayWelcomeDialog
import com.remiboulier.rocketboard.model.Rocket
import com.remiboulier.rocketboard.network.NetworkState
import com.remiboulier.rocketboard.network.SpaceXApi
import com.remiboulier.rocketboard.network.Status
import com.remiboulier.rocketboard.screen.BaseMainFragment
import com.remiboulier.rocketboard.screen.launches.LaunchesFragment
import com.remiboulier.rocketboard.util.DialogContainer
import com.remiboulier.rocketboard.util.SharedPreferencesHelper
import com.remiboulier.rocketboard.util.SharedPreferencesHelperImpl
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : BaseMainFragment() {

    private lateinit var viewModel: HomeFragmentViewModel

    private var adapter: RocketAdapter? = null
    private var container: DialogContainer = DialogContainer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        barTitle = getString(R.string.app_name)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_home, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()

        viewModel = getViewModel(
                (activity!!.application as CoreApplication).spaceXApi,
                SharedPreferencesHelperImpl(activity!!.applicationContext))
        viewModel.initActiveOnly(rocketsActiveFilter.isChecked)
        viewModel.rocketsLiveData.observe(this, Observer { updateRocketList(it!!) })
        viewModel.networkState.observe(this, Observer { onNetworkStateChange(it!!) })

        rocketsActiveFilter.setOnCheckedChangeListener { _, isChecked -> viewModel.filterResults(isChecked) }
        rocketsSwipeRefresh.setOnRefreshListener { loadData() }

        if (viewModel.isFirstTime())
            container.displayWelcomeDialog(context!!) { loadData() }
        else
            loadData()
    }

    fun onNetworkStateChange(state: NetworkState) {
        if (rocketsSwipeRefresh.isRefreshing) updateStateWhenRefreshing(state)
        else updateState(state)
    }

    override fun onStop() {
        super.onStop()
        adapter = null
        container.dismissDialog()
    }

    fun loadData() = viewModel.loadRockets()

    fun initAdapter() {
        adapter = RocketAdapter(mutableListOf(), this::goToDetails)
        rocketsRecycler.layoutManager = LinearLayoutManager(context!!, RecyclerView.VERTICAL, false)
        rocketsRecycler.adapter = adapter
    }

    fun goToDetails(rocketId: String, rocketName: String, description: String) {
        activityCallback.goToFragment(LaunchesFragment.newInstance(rocketId, rocketName, description))
    }

    fun updateRocketList(rockets: MutableList<Rocket>) {
        adapter?.updateList(rockets)
    }

    fun updateStateWhenRefreshing(networkState: NetworkState) {
        if (networkState.status != Status.RUNNING) {
            rocketsSwipeRefresh.isRefreshing = false
            if (networkState.status == Status.FAILED)
                container.displayErrorDialog(context!!, networkState.msg!!)
        }
    }

    fun updateState(networkState: NetworkState) =
            when (networkState.status) {
                Status.RUNNING -> container.displayProgressDialog(context!!)
                Status.SUCCESS -> container.dismissDialog()
                Status.FAILED -> container.displayErrorDialog(context!!, networkState.msg!!)
            }

    fun getViewModel(spaceXApi: SpaceXApi,
                     prefsHelper: SharedPreferencesHelper): HomeFragmentViewModel =
            ViewModelProviders.of(this, object : ViewModelProvider.Factory {
                override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                    @Suppress("UNCHECKED_CAST")
                    return HomeFragmentViewModel(spaceXApi, prefsHelper) as T
                }
            })[HomeFragmentViewModel::class.java]
}
