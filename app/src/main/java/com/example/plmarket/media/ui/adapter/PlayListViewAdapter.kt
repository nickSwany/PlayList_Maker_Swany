package com.example.plmarket.media.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pl_market.R
import com.example.plmarket.player.domain.models.Track

class PlayListViewAdapter(private val trackClickListener: TrackClickListener) :
    RecyclerView.Adapter<PlayListViewViewHolder>() { // походу используется в самом плейлисте
    var track = ArrayList<Track>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayListViewViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_item, parent, false)
        return PlayListViewViewHolder(view)
    }

    override fun getItemCount(): Int = track.size

    override fun onBindViewHolder(holder: PlayListViewViewHolder, position: Int) {
       holder.bind(track[position])
        holder.itemView.setOnClickListener {
            trackClickListener.onTrackClick(track[position])
        }
        holder.itemView.setOnLongClickListener {
            trackClickListener.onItemClick(track[position].trackId)
            true
        }

    }

}