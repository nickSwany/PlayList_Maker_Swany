package com.example.plmarket.player.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.pl_market.R
import com.example.pl_market.databinding.ActivityPlayerBinding
import com.example.plmarket.player.domain.StatePlayer
import com.example.plmarket.player.ui.viewModel.PlayerViewModel
import com.example.plmarket.search.ui.fragment.SearchFragment.Companion.EXTRA_ARTIST_NAME
import com.example.plmarket.search.ui.fragment.SearchFragment.Companion.EXTRA_ART_TRACK
import com.example.plmarket.search.ui.fragment.SearchFragment.Companion.EXTRA_COUNTRY
import com.example.plmarket.search.ui.fragment.SearchFragment.Companion.EXTRA_COllECTION_NAME
import com.example.plmarket.search.ui.fragment.SearchFragment.Companion.EXTRA_GENRE_NAME
import com.example.plmarket.search.ui.fragment.SearchFragment.Companion.EXTRA_SONG
import com.example.plmarket.search.ui.fragment.SearchFragment.Companion.EXTRA_TIME_MILLIS
import com.example.plmarket.search.ui.fragment.SearchFragment.Companion.EXTRA_TRACK_NAME
import com.example.plmarket.search.ui.fragment.SearchFragment.Companion.EXTRA_YEAR
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*


class PlayerActivity : AppCompatActivity() {

    companion object {
        private const val DEFAULT_TIME_LEFT = "00:00"

        fun createArgs(
            trackName: String?,
            artistName: String?,
            trackTimeMillis: String?,
            artworkUrl100: String?,
            collectionName: String?,
            releaseDate: String?,
            primaryGenreName: String?,
            country: String?,
            previewUrl: String?
        ): Bundle = bundleOf(
            EXTRA_ARTIST_NAME to artistName,
            EXTRA_COUNTRY to country,
            EXTRA_COllECTION_NAME to collectionName,
            EXTRA_SONG to previewUrl,
            EXTRA_TIME_MILLIS to trackTimeMillis,
            EXTRA_TRACK_NAME to trackName,
            EXTRA_ART_TRACK to artworkUrl100,
            EXTRA_YEAR to releaseDate,
            EXTRA_GENRE_NAME to primaryGenreName,
        )
    }

    private lateinit var binding: ActivityPlayerBinding
    private var songurl: String = ""
    private val viewModel: PlayerViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        songurl = intent.getStringExtra(EXTRA_SONG).toString()
        viewModel.preparePlayer(songurl)

        binding.playButton.setOnClickListener {
            viewModel.playStart()
        }

        binding.tollBar.setOnClickListener {
            finish()
        }

        viewModel.checkState.observe(this) {
            checkState(it)
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
        if (!urlImage.isNullOrEmpty()) {
            viewModel.getCoverArtwork(urlImage)
        }

        viewModel.coverArtwork.observe(this) {
            val url = it

            val cornerSize = resources.getDimensionPixelSize(R.dimen.radius_8)
            Glide.with(this)
                .load(getCoverArtwork(url))
                .centerCrop()
                .placeholder(R.drawable.error_paint_internet)
                .transform(RoundedCorners(cornerSize))
                .into(binding.trackImage)
        }

        val data = intent.getStringExtra(EXTRA_YEAR).toString()
        viewModel.correctDataSong(data)
        viewModel.dataSong.observe(this) {
            binding.year.text = it
        }


        val time = intent.getStringExtra(EXTRA_TIME_MILLIS)

        if (time != null) {
            viewModel.correctTimeSong(time)
            viewModel.timeSong.observe(this) {
                binding.durationTime.text = it
            }
        }

        viewModel.secondCounter.observe(this) { time ->
            binding.timeLeft.text = time
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.onPause()
    }

    fun getYearFromDatString(dataString: String): String? {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        val data = inputFormat.parse(dataString)
        val outputFormat = SimpleDateFormat("yyyy", Locale.getDefault())
        return data?.let { outputFormat.format(it) }
    }

    private fun checkState(state: StatePlayer) {
        when (state) {
            StatePlayer.STATE_PLAYING -> {
                binding.playButton.setImageResource(R.drawable.button_pause)
            }
            StatePlayer.STATE_PAUSED, StatePlayer.STATE_DEFAULT -> {
                binding.playButton.setImageResource(R.drawable.button_play)
            }
            StatePlayer.STATE_PREPARED -> {
                binding.playButton.setImageResource(R.drawable.button_play)
                binding.timeLeft.text = DEFAULT_TIME_LEFT
            }
        }
    }
}