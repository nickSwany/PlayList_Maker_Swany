package com.example.plmarket.media.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.media.ExifInterface
import android.util.TypedValue
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.pl_market.R
import com.example.pl_market.databinding.PlayListItemBinding
import com.example.plmarket.media.domain.module.PlayList

class PlayListViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    private val binding = PlayListItemBinding.bind(view)

    private fun dpToPx(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics
        ).toInt()
    }

    @SuppressLint("SetTextI18n")
    fun bind(playList: PlayList) {
        binding.apply {
            Glide.with(itemView)
                .load(playList.uri)
                .centerCrop()
                .placeholder(R.drawable.placholder_for_play_list)
                .transform(RoundedCorners(dpToPx(8f, itemView.context)))
                .into(imagePlayList)


            namePlayList.text = playList.name
            currentTracks.text =
                playList.currentTracks.toString() + " " + endingTracks(playList.currentTracks)
        }
    }

    private fun endingTracks(count: Int): String {
        return when (count % 10) {
            1 -> view.context.getString(R.string.trek)
            in 2..4 -> view.context.getString(R.string.treka)
            else -> view.context.getString(R.string.trekov)
        }
    }
}