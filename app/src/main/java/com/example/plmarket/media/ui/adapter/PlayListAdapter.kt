package com.example.plmarket.media.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pl_market.R
import com.example.plmarket.media.domain.module.PlayList

class PlayListAdapter(private val clickListener: PlayListClickListener) :
    RecyclerView.Adapter<PlayListViewHolder>() {

    var playList = ArrayList<PlayList>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayListViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.play_list_item, parent, false)
        return PlayListViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlayListViewHolder, position: Int) {
        holder.bind(playList = playList[position])
        holder.itemView.setOnClickListener {
            clickListener.onPlayListClick(playList[position])
        }
    }

    override fun getItemCount(): Int = playList.size
}