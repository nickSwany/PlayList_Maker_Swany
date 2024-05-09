package com.example.plmarket.media.ui.adapter

import android.content.Context
import android.util.TypedValue
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.pl_market.R
import com.example.pl_market.databinding.CardItemBinding
import com.example.plmarket.player.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

class PlayListViewViewHolder(val view: View): RecyclerView.ViewHolder(view) {

    private val binding = CardItemBinding.bind(view) //тут вся эта хрень похоже тоже для внутрянки

    private fun dpToPx(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics
        ).toInt()
    }

    fun bind(track: Track) = with(binding) {

        Glide.with(itemView)
            .load(track.artworkUrl100)
            .placeholder(R.drawable.error_paint_internet)
            .centerCrop()
            .transform(RoundedCorners(dpToPx(2f, itemView.context)))
            .into(imageMusicSearch)

        binding.nameText.text = track.trackName
        binding.textArtists.text = track.artistName
        binding.timeMusicText.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis!!.toInt())
    }
}