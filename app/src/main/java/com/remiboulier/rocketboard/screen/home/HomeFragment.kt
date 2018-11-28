package com.remiboulier.rocketboard.screen.home


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.remiboulier.rocketboard.CoreApplication
import com.remiboulier.rocketboard.MainActivityCallback
import com.remiboulier.rocketboard.R
import com.remiboulier.rocketboard.extension.displayErrorDialog
import com.remiboulier.rocketboard.extension.displayProgressDialog
import com.remiboulier.rocketboard.extension.displayWelcomeDialog
import com.remiboulier.rocketboard.model.Rocket
import com.remiboulier.rocketboard.network.NetworkState
import com.remiboulier.rocketboard.network.SpaceXApi
import com.remiboulier.rocketboard.network.Status
import com.remiboulier.rocketboard.screen.details.DetailsFragment
import com.remiboulier.rocketboard.util.DialogContainer
import com.remiboulier.rocketboard.util.SharedPreferencesHelper
import com.remiboulier.rocketboard.util.SharedPreferencesHelperImpl
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    private lateinit var viewModel: HomeFragmentViewModel

    private var adapter: RocketAdapter? = null
    private var activityCallback: MainActivityCallback? = null
    private var container: DialogContainer = DialogContainer()

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            activityCallback = activity as MainActivityCallback?
        } catch (e: ClassCastException) {
            throw ClassCastException(this.javaClass.simpleName + " must implement MainActivityCallback")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_home, container, false)

    override fun onStart() {
        super.onStart()

        initAdapter()

        viewModel = getViewModel(
                (activity!!.application as CoreApplication).spaceXApi,
                SharedPreferencesHelperImpl(activity!!.applicationContext))
        viewModel.rocketsLiveData.observe(this, Observer { updateRocketList(it!!) })
        viewModel.networkState.observe(this, Observer { updateState(it!!) })

        rocketsActiveFilter.setOnCheckedChangeListener { _, isChecked -> filterResult(isChecked) }

        if (viewModel.isFirstTime())
            displayWelcomeDialog()
        else
            loadData()
    }

    override fun onStop() {
        super.onStop()
        adapter = null
        container.dismissDialog()
    }

    private fun filterResult(checked: Boolean) {
        viewModel.filterResults(checked)
    }

    fun loadData() = viewModel.loadRockets()

    fun initAdapter() {
        adapter = RocketAdapter(mutableListOf(), this::goToDetails)
        rocketRecycler.layoutManager = LinearLayoutManager(context!!, RecyclerView.VERTICAL, false)
        rocketRecycler.adapter = adapter
    }

    fun updateState(networkState: NetworkState) =
            when (networkState.status) {
                Status.RUNNING -> displayProgress()
                Status.SUCCESS -> hideProgress()
                Status.FAILED -> displayError(networkState.msg!!)
            }

    fun goToDetails(rocketId: String) {
        activityCallback?.goToFragment(DetailsFragment.newInstance(rocketId))
    }

    fun updateRocketList(rockets: MutableList<Rocket>) {
        adapter?.updateList(rockets)
    }

    fun displayWelcomeDialog() = container.displayWelcomeDialog(context!!) { loadData() }

    fun displayProgress() = container.displayProgressDialog(context!!)

    fun hideProgress() = container.dismissDialog()

    fun displayError(@StringRes msg: Int) = container.displayErrorDialog(context!!, msg)

    fun getViewModel(spaceXApi: SpaceXApi,
                     prefsHelper: SharedPreferencesHelper): HomeFragmentViewModel =
            ViewModelProviders.of(this, object : ViewModelProvider.Factory {
                override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                    @Suppress("UNCHECKED_CAST")
                    return HomeFragmentViewModel(spaceXApi, prefsHelper) as T
                }
            })[HomeFragmentViewModel::class.java]
}
