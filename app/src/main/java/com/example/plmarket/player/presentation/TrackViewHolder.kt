package com.example.plmarket.player.presentation

import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.plmarket.player.domain.models.Track
import java.text.SimpleDateFormat
import com.bumptech.glide.request.RequestOptions
import com.example.pl_market.R
import java.util.*

class TrackViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context)
        .inflate(R.layout.card_item, parent, false)
) {
    private val trackImageView: ImageView = itemView.findViewById(R.id.image_music_search)
    private val trackNameTextView: TextView = itemView.findViewById(R.id.name_text)
    private val artistTextView: TextView = itemView.findViewById(R.id.text_artists)
    private val timeTextView: TextView = itemView.findViewById(R.id.time_music_text)

    fun dpToPx(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics
        ).toInt()
    }

    fun bind(track: Track) {

        val requestOptions = RequestOptions()
            .placeholder(R.drawable.error_paint_internet)
            .centerCrop()
            .transform(RoundedCorners(dpToPx(2f, itemView.context)))

        Glide.with(itemView)
            .load(track.artworkUrl100)
            .apply(requestOptions)
            .into(trackImageView)

        trackNameTextView.text = track.trackName
        artistTextView.text = track.artistName
        timeTextView.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis.toInt())
    }
}