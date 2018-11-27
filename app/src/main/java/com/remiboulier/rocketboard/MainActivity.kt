package com.remiboulier.rocketboard

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.remiboulier.rocketboard.extension.displayErrorDialog
import com.remiboulier.rocketboard.extension.displayProgressDialog
import com.remiboulier.rocketboard.extension.displayWelcomeDialog
import com.remiboulier.rocketboard.model.Rocket
import com.remiboulier.rocketboard.network.NetworkState
import com.remiboulier.rocketboard.network.SpaceXApi
import com.remiboulier.rocketboard.network.Status
import com.remiboulier.rocketboard.util.DialogContainer
import com.remiboulier.rocketboard.util.SharedPreferencesHelper
import com.remiboulier.rocketboard.util.SharedPreferencesHelperImpl
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainActivityViewModel
    private var adapter: RocketAdapter? = null

    private var container: DialogContainer = DialogContainer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initAdapter()

        viewModel = getViewModel(
                (application as CoreApplication).spaceXApi,
                SharedPreferencesHelperImpl(applicationContext))
        viewModel.rocketsLiveData.observe(this, Observer { updateRocketList(it!!) })
        viewModel.networkState.observe(this, Observer { updateState(it!!) })

        rocketsActiveFilter.setOnCheckedChangeListener { _, isChecked -> filterResult(isChecked) }

        if (viewModel.isFirstTime())
            displayWelcomeDialog()
        else
            loadData()
    }

    private fun filterResult(checked: Boolean) {
        viewModel.filterResults(checked)
    }

    override fun onStop() {
        super.onStop()
        adapter = null
        container.dismissDialog()
    }

    fun loadData() = viewModel.loadRockets()

    fun initAdapter() {
        adapter = RocketAdapter(mutableListOf(), this::goToDetails)
        rocketRecycler.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        rocketRecycler.adapter = adapter
    }

    fun updateState(networkState: NetworkState) =
            when (networkState.status) {
                Status.RUNNING -> displayProgress()
                Status.SUCCESS -> hideProgress()
                Status.FAILED -> displayError(networkState.msg)
            }

    fun goToDetails(rocketId: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun updateRocketList(rockets: MutableList<Rocket>) {
        adapter?.updateList(rockets)
    }

    fun displayWelcomeDialog() = container.displayWelcomeDialog(this) { loadData() }

    fun displayProgress() = container.displayProgressDialog(this)

    fun hideProgress() = container.dismissDialog()

    fun displayError(msg: String?) = container.displayErrorDialog(this, msg)

    fun getViewModel(spaceXApi: SpaceXApi,
                     prefsHelper: SharedPreferencesHelper): MainActivityViewModel =
            ViewModelProviders.of(this, object : ViewModelProvider.Factory {
                override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                    @Suppress("UNCHECKED_CAST")
                    return MainActivityViewModel(spaceXApi, prefsHelper) as T
                }
            })[MainActivityViewModel::class.java]
}
