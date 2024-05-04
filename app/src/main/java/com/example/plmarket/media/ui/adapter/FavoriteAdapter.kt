package com.example.plmarket.media.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pl_market.R
import com.example.plmarket.player.domain.models.Track
import com.example.plmarket.search.ui.adapter.TrackClickListener

class FavoriteAdapter(private val clickListener: TrackClickListener) :
    RecyclerView.Adapter<FavoriteViewHolder>() {

    var trackListFavorite = ArrayList<Track>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_item, parent, false)
        return FavoriteViewHolder(view)
    }

    override fun getItemCount(): Int = trackListFavorite.size

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(trackListFavorite[position])
        holder.itemView.setOnClickListener { clickListener.onTrackClick(trackListFavorite[position]) }
    }
}