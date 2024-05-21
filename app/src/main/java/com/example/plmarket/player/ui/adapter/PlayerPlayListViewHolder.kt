package com.example.plmarket.player.ui.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.pl_market.R
import com.example.pl_market.databinding.PlayerPlayListItemBinding
import com.example.plmarket.media.domain.module.PlayList

class PlayerPlayListViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    private val binding = PlayerPlayListItemBinding.bind(view)

    fun bind(playList: PlayList) = with(binding) {
        val cornerSize = itemView.resources.getDimensionPixelSize(R.dimen.otstyp_8)
        Glide.with(itemView)
            .load(playList.uri)
            .centerCrop()
            .placeholder(R.drawable.error_paint_internet)
            .transform(RoundedCorners(cornerSize))
            .into(imageMusic)

        textPlayList.text = playList.name
        countTrack.text =
            playList.currentTracks.toString() + " " + endingTracks(playList.currentTracks)
    }

    private fun endingTracks(count: Int): String {
        return when (count % 10) {
            1 -> view.context.getString(R.string.trek)
            in 2..4 -> view.context.getString(R.string.treka)
            else -> view.context.getString(R.string.trekov)
        }
    }
}
