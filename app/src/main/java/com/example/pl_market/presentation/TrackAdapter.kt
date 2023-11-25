package com.example.pl_market.presentation

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pl_market.domain.models.Track

class TrackAdapter(private val tracks: ArrayList<Track>, private var itemClickListener: (Track) -> Unit): RecyclerView.Adapter<TrackViewHolder> (){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder = TrackViewHolder(parent)
    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position])
        itemClickListener.let { clickListener ->
            holder.itemView.setOnClickListener {
                clickListener(tracks[position])
            }
        }
    }
    override fun getItemCount(): Int {
        return tracks.size
    }
}
