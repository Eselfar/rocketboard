package com.remiboulier.rocketboard.screen.launches

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.remiboulier.rocketboard.R
import com.remiboulier.rocketboard.extension.toReadableDate
import com.remiboulier.rocketboard.extension.toString
import com.remiboulier.rocketboard.model.Launch
import com.remiboulier.rocketboard.util.GlideRequests
import kotlinx.android.synthetic.main.item_recycler_launches.view.*

class LaunchAdapter(private val launches: MutableList<Launch>,
                    private val glide: GlideRequests)
    : RecyclerView.Adapter<LaunchViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): LaunchViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_recycler_launches, parent, false)

        return LaunchViewHolder(view)
    }

    override fun getItemCount(): Int = launches.size

    override fun onBindViewHolder(holder: LaunchViewHolder, position: Int) {
        holder.bind(launches[position], glide)
    }

    fun updateList(newLaunches: MutableList<Launch>) {
        launches.clear()
        launches.addAll(newLaunches)
        notifyDataSetChanged()
    }
}

class LaunchViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

    fun bind(launch: Launch,
             glide: GlideRequests) = with(view) {
        val unknown = context.getString(R.string.unknown)
        val date = launch.launchDateUnix?.toReadableDate() ?: unknown
        val success = launch.launchSuccess?.toString(context) ?: unknown

        launchMissionName.text = context.getString(R.string.mission, launch.missionName)
        launchDate.text = context.getString(R.string.date, date)
        launchIsSuccess.text = context.getString(R.string.success, success)

        if (launch.links.missionPatchSmall?.isBlank() == false)
            displayImage(launchImage, launch.links.missionPatchSmall, glide)
    }

    private fun displayImage(target: ImageView, image: String, glide: GlideRequests) {
        glide.load(image)
                .placeholder(R.mipmap.ic_launcher)
                .into(target)
    }
}
