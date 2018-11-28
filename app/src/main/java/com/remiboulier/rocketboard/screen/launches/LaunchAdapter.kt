package com.remiboulier.rocketboard.screen.launches

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.remiboulier.rocketboard.R
import com.remiboulier.rocketboard.model.Launch
import kotlinx.android.synthetic.main.item_recycler_launches.view.*

class LaunchAdapter(private val launches: MutableList<Launch>) : RecyclerView.Adapter<LaunchViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): LaunchViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_recycler_launches, parent, false)

        return LaunchViewHolder(view)
    }

    override fun getItemCount(): Int = launches.size

    override fun onBindViewHolder(holder: LaunchViewHolder, position: Int) {
        holder.bind(launches[position])
    }

    fun updateList(newLaunches: MutableList<Launch>) {
        launches.clear()
        launches.addAll(newLaunches)
        notifyDataSetChanged()
    }
}

class LaunchViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

    fun bind(launch: Launch) = with(view) {
        launchMissionName.text = launch.missionName
        launchDate.text = launch.launchDateUtc
        launchIsSuccess.text = launch.launchSuccess?.toString() ?: context.getString(R.string.unknown)
    }
}
