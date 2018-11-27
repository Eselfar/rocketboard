package com.remiboulier.rocketboard

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
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

        if (viewModel.isFirstTime())
            displayWelcomeDialog { loadData() }
        else
            loadData()
    }

    private fun displayWelcomeDialog(onPositive: () -> Unit) {
        container.showDialog(MaterialDialog.Builder(this)
                .title(R.string.app_name)
                .content(R.string.welcome_message)
                .cancelable(false)
                .positiveText(R.string.got_it)
                .onPositive { _, _ -> onPositive() }
                .build())
    }

    fun loadData() {
        viewModel.loadRockets()
    }

    private fun updateState(networkState: NetworkState) {
        when (networkState.status) {
            Status.RUNNING -> displayProgress()
            Status.SUCCESS -> hideProgress()
            Status.FAILED -> displayError(networkState.msg)
        }
    }

    private fun displayProgress() {
        container.showDialog(MaterialDialog.Builder(this)
                .title(R.string.app_name)
                .content(R.string.please_wait)
                .cancelable(false)
                .progress(true, 0)
                .build())
    }

    private fun hideProgress() {
        container.dismissDialog()
    }

    private fun displayError(msg: String?) {
        container.showDialog(MaterialDialog.Builder(this)
                .title(title)
                .cancelable(true)
                .content(msg ?: getString(R.string.an_error_occured))
                .positiveText(R.string.got_it)
                .build())
    }

    fun initAdapter() {
        adapter = RocketAdapter(mutableListOf(), this::goToDetails)
        homeRecycler.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        homeRecycler.adapter = adapter
    }

    private fun goToDetails(rocketId: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun updateRocketList(rockets: MutableList<Rocket>) {
        adapter?.updateList(rockets)
    }

    override fun onStop() {
        super.onStop()
        adapter = null
        container.dismissDialog()
    }

    fun getViewModel(spaceXApi: SpaceXApi,
                     prefsHelper: SharedPreferencesHelper): MainActivityViewModel =
            ViewModelProviders.of(this, object : ViewModelProvider.Factory {
                override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                    @Suppress("UNCHECKED_CAST")
                    return MainActivityViewModel(spaceXApi, prefsHelper) as T
                }
            })[MainActivityViewModel::class.java]
}
