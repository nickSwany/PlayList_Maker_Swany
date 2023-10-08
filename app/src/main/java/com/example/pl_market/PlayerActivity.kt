package com.example.pl_market

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.pl_market.databinding.ActivityPlayerBinding
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*

const val EXTRA_TRACK_NAME = "trackName"
const val EXTRA_ARTIST_NAME = "artistName"
const val EXTRA_TIME_MILLIS = "timeMillis"
const val EXTRA_ART_TRACK = "artTrack"
const val EXTRA_COUNTRY = "country"
const val EXTRA_YEAR = "year"
const val EXTRA_GENRE_NAME = "genreName"
const val EXTRA_COllECTION_NAME = "collectionName"


class PlayerActivity : AppCompatActivity() {

    companion object {
        private const val DEFAULT_TIME_LEFT = "00:00"
    }

    private lateinit var binding: ActivityPlayerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tollBar.setOnClickListener {
            finish()
        }

        binding.apply {
            trackName.text = intent.getStringExtra(EXTRA_TRACK_NAME)
            groupName.text = intent.getStringExtra(EXTRA_ARTIST_NAME)
            countryText.text = intent.getStringExtra(EXTRA_COUNTRY)
            genreText.text = intent.getStringExtra(EXTRA_GENRE_NAME)
            yearNumber.text = getYearFromDatString(intent.getStringExtra(EXTRA_YEAR) ?: "")
            binding.timeLeft.text = DEFAULT_TIME_LEFT
            durationTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(
                intent.getStringExtra(
                    EXTRA_TIME_MILLIS
                )?.toInt() ?: return
            )


            val albumText = intent.getStringExtra(EXTRA_COllECTION_NAME)
            if (albumText != null) {
                albumName.text = albumText
            } else {
                albumName.isVisible = false
                album.isVisible = false
            }
        }

        fun getCoverArtwork(artworkUrl100: String?) =
            artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg")

        val urlImage = intent.getStringExtra(EXTRA_ART_TRACK)

        val cornerSize = resources.getDimensionPixelSize(R.dimen.radius_8)
        Glide.with(this)
            .load(getCoverArtwork(urlImage))
            .centerCrop()
            .placeholder(R.drawable.error_paint_internet)
            .transform(RoundedCorners(cornerSize))
            .into(binding.trackImage)

    }

    fun getYearFromDatString(dataString: String): String? {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        val data = inputFormat.parse(dataString)
        val outputFormat = SimpleDateFormat("yyyy", Locale.getDefault())
        return data?.let { outputFormat.format(it) }
    }
}