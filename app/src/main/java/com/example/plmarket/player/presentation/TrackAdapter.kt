package com.example.plmarket.player.presentation

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.plmarket.player.domain.models.Track

class TrackAdapter(private val tracks: ArrayList<Track>, private var itemClickListener: (Track) -> Unit): RecyclerView.Adapter<TrackViewHolder> (){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder = TrackViewHolder(parent)
    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener{
            itemClickListener(tracks[position])
        }
    }
    override fun getItemCount(): Int {
        return tracks.size
    }
}
