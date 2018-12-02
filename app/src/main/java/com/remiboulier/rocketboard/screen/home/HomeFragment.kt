package com.remiboulier.rocketboard.screen.home


import android.arch.lifecycle.Observer
import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.remiboulier.rocketboard.R
import com.remiboulier.rocketboard.extension.displayErrorDialog
import com.remiboulier.rocketboard.extension.displayWelcomeDialog
import com.remiboulier.rocketboard.network.NetworkState
import com.remiboulier.rocketboard.network.Status
import com.remiboulier.rocketboard.room.entity.RocketEntity
import com.remiboulier.rocketboard.screen.BaseMainFragment
import com.remiboulier.rocketboard.screen.launches.LaunchesFragment
import com.remiboulier.rocketboard.util.DialogContainer
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_home.*
import javax.inject.Inject

class HomeFragment : BaseMainFragment() {

    @Inject
    lateinit var viewModel: HomeFragmentViewModel

    private var adapter: RocketAdapter? = null
    private var container: DialogContainer = DialogContainer()

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)

        viewModel.getRocketsLiveData().observe(this, Observer { updateRocketList(it!!) })
        viewModel.getNetworkState().observe(this, Observer { onNetworkStateChange(it!!) })
    }

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

        rocketsActiveFilter.setOnCheckedChangeListener { _, isChecked -> viewModel.updateActiveOnly(isChecked) }
        rocketsSwipeRefresh.setOnRefreshListener { loadData(true) }

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

    fun loadData(forceRefresh: Boolean = false) = viewModel.loadRockets(forceRefresh)

    fun initAdapter() {
        adapter = RocketAdapter(mutableListOf(), this::goToDetails)
        rocketsRecycler.layoutManager = LinearLayoutManager(context!!, RecyclerView.VERTICAL, false)
        rocketsRecycler.adapter = adapter
    }

    fun goToDetails(rocketId: String, rocketName: String, description: String) {
        activityCallback.goToFragment(LaunchesFragment.newInstance(rocketId, rocketName, description))
    }

    fun updateRocketList(rockets: List<RocketEntity>) {
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
                Status.RUNNING -> showProgress()
                Status.SUCCESS -> hideProgress()
                Status.FAILED -> container.displayErrorDialog(context!!, networkState.msg!!)
            }

    fun showProgress() {
        rocketsProgress.visibility = View.VISIBLE
    }

    fun hideProgress() {
        rocketsProgress.visibility = View.INVISIBLE
    }
}
