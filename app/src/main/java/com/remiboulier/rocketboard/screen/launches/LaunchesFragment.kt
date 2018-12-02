package com.remiboulier.rocketboard.screen.launches


import android.arch.lifecycle.Observer
import android.content.Context
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.brandongogetap.stickyheaders.StickyLayoutManager
import com.github.mikephil.charting.data.BarEntry
import com.remiboulier.rocketboard.R
import com.remiboulier.rocketboard.extension.displayErrorDialog
import com.remiboulier.rocketboard.network.NetworkState
import com.remiboulier.rocketboard.network.Status
import com.remiboulier.rocketboard.room.entity.LaunchEntity
import com.remiboulier.rocketboard.screen.BaseMainFragment
import com.remiboulier.rocketboard.util.BundleConstants
import com.remiboulier.rocketboard.util.DialogContainer
import com.remiboulier.rocketboard.util.GlideApp
import com.remiboulier.rocketboard.util.GlideRequests
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_launches.*
import javax.inject.Inject

class LaunchesFragment : BaseMainFragment() {

    @Inject
    lateinit var viewModel: LaunchesFragmentViewModel

    private lateinit var rocketId: String

    private var adapter: LaunchAdapter? = null
    private var container: DialogContainer = DialogContainer()

    companion object {
        fun newInstance(rocketId: String,
                        rocketName: String,
                        description: String): LaunchesFragment {
            return LaunchesFragment().apply {
                val bundle = Bundle()
                bundle.putString(BundleConstants.ROCKET_ID, rocketId)
                bundle.putString(BundleConstants.ROCKET_NAME, rocketName)
                bundle.putString(BundleConstants.ROCKET_DESCRIPTION, description)
                arguments = bundle
            }
        }
    }

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)

        rocketId = arguments?.getString(BundleConstants.ROCKET_ID)
                ?: throw MissingArgumentException()

        viewModel.getLaunchesLiveData().observe(this, Observer { updateUI(it!!) })
        viewModel.getLaunchesPerYearLiveData().observe(this, Observer { updateChart(it!!) })
        viewModel.getNetworkState().observe(this, Observer { onNetworkStateChange(it!!) })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        barTitle = arguments?.getString(BundleConstants.ROCKET_NAME)
                ?: ""
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_launches, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val description = arguments?.getString(BundleConstants.ROCKET_DESCRIPTION)
                ?: throw MissingArgumentException()

        launchesSwipeRefresh.setOnRefreshListener { loadData(true) }
        initAdapter(description, GlideApp.with(activity!!))

        loadData()
    }

    fun onNetworkStateChange(state: NetworkState) {
        if (launchesSwipeRefresh.isRefreshing) updateStateWhenRefreshing(state)
        else updateState(state)
    }

    fun updateChart(launchesPerYear: List<BarEntry>) = adapter?.updateChart(launchesPerYear)
    fun updateUI(launches: List<LaunchEntity>) = adapter?.updateLaunches(launches)

    override fun onStop() {
        super.onStop()
        adapter = null
        container.dismissDialog()
    }

    fun loadData(forceRefresh: Boolean = false) = viewModel.loadLaunches(rocketId, forceRefresh)

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
                Status.RUNNING -> showProgress()
                Status.SUCCESS -> hideProgress()
                Status.FAILED -> container.displayErrorDialog(context!!, networkState.msg!!)
            }

    fun showProgress() {
        launchesProgress.visibility = View.VISIBLE
    }

    fun hideProgress() {
        launchesProgress.visibility = View.INVISIBLE
    }
}

class MissingArgumentException : RuntimeException("Missing required Fragment argument")
