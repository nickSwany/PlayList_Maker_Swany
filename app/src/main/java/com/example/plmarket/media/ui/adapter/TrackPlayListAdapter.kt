package com.example.plmarket.media.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pl_market.R
import com.example.plmarket.media.domain.module.PlayList
import com.example.plmarket.player.domain.models.Track

class TrackPlayListAdapter(private val clickListener: TrackPlayListLongClickListener) :
    RecyclerView.Adapter<TrackPlayListViewHolder>() {

    var tracks = ArrayList<Track>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackPlayListViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.card_item, parent, false)
        return TrackPlayListViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackPlayListViewHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener {
            clickListener.onClick(tracks[position])
        }
        holder.itemView.setOnLongClickListener {
            clickListener.onLongClick(tracks[position].trackId)
            true
        }
    }

    override fun getItemCount(): Int = tracks.size
}