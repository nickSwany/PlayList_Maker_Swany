package com.example.plmarket.search.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pl_market.R
import com.example.plmarket.player.domain.models.Track

class HistoryAdapter(private val clickListener: TrackClickListener) :
    RecyclerView.Adapter<TrackViewHolder>() {

    var trackListHistory = ArrayList<Track>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_item, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(trackListHistory[position])
        holder.itemView.setOnClickListener { clickListener.onTrackClick(trackListHistory[position]) }
    }

    override fun getItemCount(): Int = trackListHistory.size
}