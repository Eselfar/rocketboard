package com.remiboulier.rocketboard.screen.launches


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.brandongogetap.stickyheaders.StickyLayoutManager
import com.github.mikephil.charting.data.BarEntry
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
import com.remiboulier.rocketboard.util.GlideApp
import com.remiboulier.rocketboard.util.GlideRequests
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
                              savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_launches, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rocketId = arguments?.getString(BundleConstants.ROCKET_ID)
                ?: throw MissingArgumentException()
        val description = arguments?.getString(BundleConstants.ROCKET_DESCRIPTION)
                ?: throw MissingArgumentException()

        viewModel = getViewModel((activity!!.application as CoreApplication).spaceXApi, rocketId)
        viewModel.launchesLiveData.observe(this, Observer { updateUI(it!!) })
        viewModel.launchesPerYearLiveData.observe(this, Observer { updateChart(it!!) })
        viewModel.networkState.observe(this, Observer { onNetworkStateChange(it!!) })

        launchesSwipeRefresh.setOnRefreshListener { loadData() }
        initAdapter(description, GlideApp.with(activity!!))

        loadData()
    }

    fun onNetworkStateChange(state: NetworkState) {
        if (launchesSwipeRefresh.isRefreshing) updateStateWhenRefreshing(state)
        else updateState(state)
    }

    fun updateChart(launchesPerYear: List<BarEntry>) = adapter?.updateChart(launchesPerYear)
    fun updateUI(launches: MutableList<Launch>) = adapter?.updateLaunches(launches)

    override fun onStop() {
        super.onStop()
        adapter = null
        container.dismissDialog()
    }

    fun loadData() = viewModel.loadLaunches()

    fun initAdapter(description: String,
                    glideRequests: GlideRequests) {
        adapter = LaunchAdapter(mutableListOf(), description, glideRequests)

        val itemDecorator = DividerItemDecoration(context!!, DividerItemDecoration.VERTICAL)
        itemDecorator.setDrawable(ContextCompat.getDrawable(context!!, R.drawable.decoration_vertical_space)!!)
        launchesRecycler.addItemDecoration(itemDecorator)
        launchesRecycler.layoutManager = StickyLayoutManager(context!!, RecyclerView.VERTICAL, false, adapter)
        launchesRecycler.adapter = adapter
    }

    fun updateStateWhenRefreshing(networkState: NetworkState) {
        if (networkState.status != Status.RUNNING) {
            launchesSwipeRefresh.isRefreshing = false
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
                     rocketId: String)
            : LaunchesFragmentViewModel =

            ViewModelProviders.of(this, object : ViewModelProvider.Factory {
                override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                    @Suppress("UNCHECKED_CAST")
                    return LaunchesFragmentViewModel(spaceXApi, rocketId) as T
                }
            })[LaunchesFragmentViewModel::class.java]
}

class MissingArgumentException : RuntimeException("Missing required Fragment argument")
