package com.remiboulier.rocketboard.screen.launches


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.formatter.IValueFormatter
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
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_launches, container, false)
    }

    override fun onStart() {
        super.onStart()

        val rocketId = arguments?.getString(BundleConstants.ROCKET_ID)
                ?: throw MissingArgumentException()
        val description = arguments?.getString(BundleConstants.ROCKET_DESCRIPTION)
                ?: throw MissingArgumentException()

        viewModel = getViewModel((activity!!.application as CoreApplication).spaceXApi, rocketId)
        viewModel.launchesLiveData.observe(this, Observer { updateUI(it!!, description) })
        viewModel.launchesPerYearLiveData.observe(this, Observer { updateChart(it!!) })
        viewModel.networkState.observe(this, Observer { updateState(it!!) })

        initAdapter(GlideApp.with(activity!!))

        loadData()
    }

    private fun updateChart(launchesPerYear: List<BarEntry>) {
        if (launchesPerYear.isNotEmpty()) {
            val dataSet = BarDataSet(launchesPerYear, getString(R.string.number_of_launches))
            dataSet.valueFormatter = IValueFormatter { value, _, _, _ -> value.toInt().toString() }

            val barData = BarData(dataSet)
            barData.barWidth = 0.9f

            val rightAxis = launchesChart.axisRight
            rightAxis.isEnabled = false

            val leftAxis = launchesChart.axisLeft
            leftAxis.setDrawGridLines(true)

            val xAxis = launchesChart.xAxis
            xAxis.granularity = 1f
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.isGranularityEnabled = true
            xAxis.valueFormatter = IAxisValueFormatter { value, _ -> value.toInt().toString() }
            xAxis.setDrawGridLines(false)

            launchesChart.setFitBars(false)
            launchesChart.data = barData
            launchesChart.description.text = ""
        }

        launchesChart.invalidate()
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

    fun initAdapter(glideRequests: GlideRequests) {
        adapter = LaunchAdapter(mutableListOf(), glideRequests)

        val itemDecorator = DividerItemDecoration(context!!, DividerItemDecoration.VERTICAL)
        itemDecorator.setDrawable(ContextCompat.getDrawable(context!!, R.drawable.decoration_vertical_space)!!)
        launchesRecycler.addItemDecoration(itemDecorator)
        launchesRecycler.layoutManager = LinearLayoutManager(context!!, RecyclerView.VERTICAL, false)
        launchesRecycler.adapter = adapter
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
