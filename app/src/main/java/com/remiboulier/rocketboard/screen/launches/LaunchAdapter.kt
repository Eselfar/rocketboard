package com.remiboulier.rocketboard.screen.launches

import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.brandongogetap.stickyheaders.exposed.StickyHeader
import com.brandongogetap.stickyheaders.exposed.StickyHeaderHandler
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.formatter.IValueFormatter
import com.remiboulier.rocketboard.R
import com.remiboulier.rocketboard.extension.toReadableDate
import com.remiboulier.rocketboard.extension.toString
import com.remiboulier.rocketboard.network.dto.LaunchDto
import com.remiboulier.rocketboard.util.GlideRequests
import kotlinx.android.synthetic.main.item_recycler_chart.view.*
import kotlinx.android.synthetic.main.item_recycler_description.view.*
import kotlinx.android.synthetic.main.item_recycler_launches.view.*

class LaunchAdapter(private val items: MutableList<Any>,
                    private val description: String,
                    private val glide: GlideRequests)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>(), StickyHeaderHandler {


    override fun getAdapterData(): MutableList<*> {
        return items
    }

    private val launches = mutableListOf<Any>()
    private val chartEntries = mutableListOf<BarEntry>()

    companion object {
        const val CHART = 0
        const val DESCRIPTION = 1
        const val HEADER_DATE = 2
        const val LAUNCH = 3
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
            when (viewType) {
                CHART -> ChartViewHolder(inflateLayout(parent, R.layout.item_recycler_chart))
                DESCRIPTION -> DescriptionViewHolder(inflateLayout(parent, R.layout.item_recycler_description) as TextView)
                HEADER_DATE -> HeaderDateViewHolder(inflateLayout(parent, R.layout.item_recycler_header_date) as TextView)
                LAUNCH -> LaunchViewHolder(inflateLayout(parent, R.layout.item_recycler_launches))
                else -> throw InvalidViewTypeException()
            }

    override fun getItemViewType(position: Int): Int =
            when (items[position]) {
                is ChartItem -> CHART
                is DescriptionItem -> DESCRIPTION
                is YearHeader -> HEADER_DATE
                is LaunchDto -> LAUNCH
                else -> throw InvalidItemException()
            }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ChartViewHolder -> holder.bind(chartEntries)
            is DescriptionViewHolder -> holder.bind(description)
            is HeaderDateViewHolder -> holder.bind(items[position] as YearHeader)
            is LaunchViewHolder -> holder.bind(items[position] as LaunchDto, glide)
        }
    }

    fun inflateLayout(parent: ViewGroup, @LayoutRes layout: Int) =
            LayoutInflater.from(parent.context)
                    .inflate(layout, parent, false)!!

    private fun updateItemList() {
        items.clear()
        items.add(ChartItem())
        items.add(DescriptionItem())
        items.addAll(launches)

        notifyDataSetChanged()
    }

    fun updateLaunches(newLaunches: MutableList<LaunchDto>) {
        launches.clear()
        newLaunches.sortedBy { launch -> launch.launchDateUnix }
        var prevYear = 0
        for (launch in newLaunches) {
            if (launch.launchYear > prevYear) {
                prevYear = launch.launchYear
                launches.add(YearHeader(prevYear.toString()))
            }
            launches.add(launch)
        }

        updateItemList()
    }

    fun updateChart(launchesPerYear: List<BarEntry>) {
        chartEntries.clear()
        chartEntries.addAll(launchesPerYear)

        notifyDataSetChanged()
    }
}

class ChartItem

class DescriptionItem

data class YearHeader(val year: String) : StickyHeader

class InvalidItemException : RuntimeException("Item type not supported")

class InvalidViewTypeException : RuntimeException("View type not supported")

class DescriptionViewHolder(private val view: TextView) : RecyclerView.ViewHolder(view) {
    fun bind(description: String) {
        view.launchesDescription.text = description
    }
}

class ChartViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

    fun bind(launchesPerYear: List<BarEntry>) = with(view) {
        if (launchesPerYear.isNotEmpty()) {
            val dataSet = BarDataSet(launchesPerYear, context.getString(R.string.number_of_launches))
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
}


class HeaderDateViewHolder(private val view: TextView) : RecyclerView.ViewHolder(view) {

    fun bind(header: YearHeader) {
        view.text = header.year
    }
}

class LaunchViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

    fun bind(launch: LaunchDto,
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
