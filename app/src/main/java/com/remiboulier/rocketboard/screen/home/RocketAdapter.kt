package com.remiboulier.rocketboard.screen.home

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.remiboulier.rocketboard.R
import com.remiboulier.rocketboard.room.entity.RocketEntity
import kotlinx.android.synthetic.main.item_recycler_rocket.view.*

class RocketAdapter(
        private val rockets: MutableList<RocketEntity>,
        private val onClick: (String, String, String) -> Unit)
    : RecyclerView.Adapter<RocketViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): RocketViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_recycler_rocket, parent, false)

        return RocketViewHolder(view)
    }

    override fun getItemCount() = rockets.size

    override fun onBindViewHolder(holder: RocketViewHolder, position: Int) {
        holder.bind(rockets[position], onClick)
    }

    fun updateList(newRockets: List<RocketEntity>) {
        rockets.clear()
        rockets.addAll(newRockets)
        notifyDataSetChanged()
    }
}

class RocketViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

    fun bind(rocket: RocketEntity, onClick: (String, String, String) -> Unit) = with(view) {
        rocketName.text = rocket.rocketName
        rocketCountry.text = view.context.getString(R.string.country, rocket.country)
        rocketEnginesCount.text = view.context.getString(R.string.number_of_engines, rocket.numberOfEngines)
        setOnClickListener { onClick(rocket.rocketId, rocket.rocketName, rocket.description) }
    }
}
