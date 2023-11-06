package com.example.pl_market

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.pl_market.databinding.ActivityPlayerBinding
import java.text.SimpleDateFormat
import java.util.*

const val EXTRA_TRACK_NAME = "trackName"
const val EXTRA_ARTIST_NAME = "artistName"
const val EXTRA_TIME_MILLIS = "timeMillis"
const val EXTRA_ART_TRACK = "artTrack"
const val EXTRA_COUNTRY = "country"
const val EXTRA_YEAR = "year"
const val EXTRA_GENRE_NAME = "genreName"
const val EXTRA_COllECTION_NAME = "collectionName"
const val EXTRA_SONG = "track_song"


class PlayerActivity : AppCompatActivity() {

    companion object {
        private const val DEFAULT_TIME_LEFT = "00:00"
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val DELAY = 1000L
    }

    private var secondsLeftTV: TextView? = null
    private lateinit var binding: ActivityPlayerBinding
    private lateinit var play: ImageView
    private var mediaPlayer = MediaPlayer()
    private var mainThreadHandler: Handler? = null

    private var playerState = STATE_DEFAULT
    private var url: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainThreadHandler = Handler(android.os.Looper.getMainLooper())

        play = findViewById(R.id.playButton)
        secondsLeftTV = findViewById(R.id.time_left)

        url = intent.getStringExtra(EXTRA_SONG).toString()
        preparePlayer()

        play.setOnClickListener {
            playbackControl()
            mainThreadHandler?.post(changeTimeProgress())
        }

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

    private fun preparePlayer() {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            play.isEnabled = true
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            binding.playButton.setImageResource(R.drawable.button_play)
            playerState = STATE_PREPARED
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        binding.playButton.setImageResource(R.drawable.button_pause)
        playerState = STATE_PLAYING
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        binding.playButton.setImageResource(R.drawable.button_play)
        playerState = STATE_PAUSED
    }

    private fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }
            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }

        }
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }

    private fun changeTimeProgress(): Runnable {
return object : Runnable {
    override fun run() {
        when (playerState) {
            STATE_PLAYING -> {
                binding.timeLeft.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(
                    mediaPlayer?.currentPosition
                )
                mainThreadHandler?.postDelayed(this, DELAY)
            }
            STATE_PAUSED -> {
                mainThreadHandler?.removeCallbacks(this)
            }
            STATE_PREPARED -> {
                binding.timeLeft.text = DEFAULT_TIME_LEFT
            }
            STATE_DEFAULT -> {
                binding.timeLeft.text = DEFAULT_TIME_LEFT
            }
        }
    }
}
    }
}