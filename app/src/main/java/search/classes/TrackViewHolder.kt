package search.classes

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.pl_market.R
import com.example.pl_market.Track

class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val trackImageView: ImageView = itemView.findViewById(R.id.image_music_search)
    private val trackNameTextView: TextView = itemView.findViewById(R.id.name_text)
    private val artistTextView: TextView = itemView.findViewById(R.id.text_artists)
    private val timeTextView: TextView = itemView.findViewById(R.id.time_music_text)

    fun bind(track: Track) {
        trackNameTextView.text = track.trackName
        artistTextView.text = track.artistName
        timeTextView.text = track.trackTime

        Glide.with(itemView)
            .load(track.artWorkUrl100)
            .placeholder(R.drawable.error_paint_internet)
            .centerCrop()
            .transform(RoundedCorners(2))
            .into(trackImageView)
        }
    }
