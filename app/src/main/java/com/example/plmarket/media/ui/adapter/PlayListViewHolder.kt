package com.example.plmarket.media.ui.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.pl_market.R
import com.example.pl_market.databinding.PlayListItemBinding
import com.example.plmarket.media.domain.module.PlayList

class PlayListViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    private val binding = PlayListItemBinding.bind(view)

    fun bind(playList: PlayList) = with(binding) {

        val cornerSize = itemView.resources.getDimensionPixelSize(R.dimen.radius_8)

        Glide.with(itemView)
            .load(playList.uri)
            .centerCrop()
            .placeholder(R.drawable.error_paint_internet)
            .transform(RoundedCorners(cornerSize))
            .into(imagePlayList)

        namePlayList.text = playList.name
        currentTracks.text = playList.currentTracks.toString()
    }
}