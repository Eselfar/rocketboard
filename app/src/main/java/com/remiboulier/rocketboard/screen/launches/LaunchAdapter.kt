package com.remiboulier.rocketboard.screen.launches

import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.remiboulier.rocketboard.R
import com.remiboulier.rocketboard.extension.toReadableDate
import com.remiboulier.rocketboard.extension.toString
import com.remiboulier.rocketboard.model.Launch
import com.remiboulier.rocketboard.util.GlideRequests
import kotlinx.android.synthetic.main.item_recycler_launches.view.*

class LaunchAdapter(private val items: MutableList<Any>,
                    private val glide: GlideRequests)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val HEADER = 0
        const val ITEM = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
            when (viewType) {
                HEADER -> HeaderViewHolder(inflateLayout(parent, R.layout.item_recycler_header_date) as TextView)
                ITEM -> LaunchViewHolder(inflateLayout(parent, R.layout.item_recycler_launches))
                else -> throw InvalidViewTypeException()
            }

    override fun getItemViewType(position: Int): Int =
            when (items[position]) {
                is YearHeader -> HEADER
                is Launch -> ITEM
                else -> throw InvalidItemException()
            }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HeaderViewHolder -> holder.bind(items[position] as YearHeader)
            is LaunchViewHolder -> holder.bind(items[position] as Launch, glide)
        }
    }

    fun inflateLayout(parent: ViewGroup, @LayoutRes layout: Int) =
            LayoutInflater.from(parent.context)
                    .inflate(layout, parent, false)

    fun updateList(newLaunches: MutableList<Launch>) {
        items.clear()
        newLaunches.sortedBy { launch -> launch.launchDateUnix }
        var prevYear = 0
        for (launch in newLaunches) {
            if (launch.launchYear > prevYear) {
                prevYear = launch.launchYear
                items.add(YearHeader(prevYear.toString()))
            }
            items.add(launch)
        }

        notifyDataSetChanged()
    }
}

data class YearHeader(val year: String)

class InvalidItemException : RuntimeException("Item type not supported")

class InvalidViewTypeException : RuntimeException("View type not supported")

class HeaderViewHolder(private val view: TextView) : RecyclerView.ViewHolder(view) {

    fun bind(header: YearHeader) {
        view.text = header.year
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
