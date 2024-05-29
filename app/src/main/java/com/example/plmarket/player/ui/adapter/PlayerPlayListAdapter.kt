package com.example.plmarket.player.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pl_market.R
import com.example.plmarket.media.domain.module.PlayList
import com.example.plmarket.media.ui.adapter.PlayListClickListener

class PlayerPlayListAdapter(private val playListClickListener: PlayListClickListener) :
    RecyclerView.Adapter<PlayerPlayListViewHolder>() {

    var playList = ArrayList<PlayList>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerPlayListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.player_play_list_item, parent, false)
        return PlayerPlayListViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlayerPlayListViewHolder, position: Int) {
        holder.bind(playList[position])
        holder.itemView.setOnClickListener { playListClickListener.onPlayListClick(playList[position]) }
    }

    override fun getItemCount(): Int = playList.size


}