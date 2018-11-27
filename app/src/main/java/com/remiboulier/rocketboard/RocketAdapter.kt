package com.remiboulier.rocketboard

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.remiboulier.rocketboard.model.Rocket
import kotlinx.android.synthetic.main.item_recycler_rocket.view.*

class RocketAdapter(
        private val rockets: MutableList<Rocket>,
        private val onClick: (String) -> Unit)
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

    fun updateList(newRockets: MutableList<Rocket>) {
        rockets.clear()
        rockets.addAll(newRockets)
        notifyDataSetChanged()
    }
}

class RocketViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

    fun bind(rocket: Rocket, onClick: (String) -> Unit) = with(view) {
        rocketName.text = rocket.rocketName
        setOnClickListener { onClick(rocket.rocketId) }
    }
}
