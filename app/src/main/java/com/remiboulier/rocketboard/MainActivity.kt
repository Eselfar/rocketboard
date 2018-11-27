package com.remiboulier.rocketboard

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.remiboulier.rocketboard.model.Rocket
import com.remiboulier.rocketboard.network.SpaceXApi
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainActivityViewModel
    private var adapter: RocketAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initAdapter()

        viewModel = getViewModel((application as CoreApplication).spaceXApi)
        viewModel.rocketsLiveData.observe(this, Observer { updateRocketList(it!!) })
        viewModel.loadRockets()
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
        adapter = null;
    }

    fun getViewModel(spaceXApi: SpaceXApi): MainActivityViewModel =
            ViewModelProviders.of(this, object : ViewModelProvider.Factory {
                override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                    @Suppress("UNCHECKED_CAST")
                    return MainActivityViewModel(spaceXApi) as T
                }
            })[MainActivityViewModel::class.java]
}
