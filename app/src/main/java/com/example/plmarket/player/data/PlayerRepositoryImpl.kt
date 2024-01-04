package com.example.plmarket.player.data

import android.media.MediaPlayer
import android.os.Looper
import com.example.pl_market.R
import com.example.pl_market.databinding.ActivityPlayerBinding
import com.example.plmarket.player.domain.StatePlayer
import com.example.plmarket.player.domain.api.PlayerListener
import com.example.plmarket.player.domain.api.PlayerRepository
import android.os.Handler
import java.text.SimpleDateFormat
import java.util.*

class PlayerRepositoryImpl : PlayerRepository {
    companion object {
        private const val DEFAULT_TIME_LEFT = "00:00"
        private const val DELAY = 1000L
    }

    private var listener: PlayerListener? = null
    private var playerState= StatePlayer.STATE_DEFAULT
    private var time = DEFAULT_TIME_LEFT
    private lateinit var binding: ActivityPlayerBinding

    val handler = Handler(Looper.getMainLooper())
    val mediaPlayer = MediaPlayer()

    override fun preparePlayer(url: String) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            binding.playButton.isEnabled = true
            playerState = StatePlayer.STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            binding.playButton.setImageResource(R.drawable.button_play)
            playerState = StatePlayer.STATE_PREPARED
        }
    }

    override fun releasePlayer() {
        mediaPlayer.release()
    }

    override fun playbackControl() {
        when (playerState) {
            StatePlayer.STATE_PLAYING -> {
                pausePlayer()
            }
            StatePlayer.STATE_PREPARED, StatePlayer.STATE_PAUSED -> {
                startPlayer()
            }

            else -> {StatePlayer.STATE_DEFAULT}
        }
    }

    override fun startPlayer() {
        mediaPlayer.start()
        binding.playButton.setImageResource(R.drawable.button_pause)
        playerState = StatePlayer.STATE_PLAYING
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
        binding.playButton.setImageResource(R.drawable.button_play)
        playerState = StatePlayer.STATE_PAUSED
    }

    override fun updateTime(time: String) {
        this.time = time

        handler.postDelayed(
            object : Runnable {
                override fun run() {
                    when (playerState) {
                        StatePlayer.STATE_PLAYING -> {
                            binding.timeLeft.text =
                                SimpleDateFormat("mm:ss", Locale.getDefault()).format(
                                    mediaPlayer.currentPosition
                                )
                            handler.postDelayed(this, DELAY) // handler?.
                        }
                        StatePlayer.STATE_PAUSED -> {
                            handler.removeCallbacks(this) // handler?.
                        }
                        StatePlayer.STATE_PREPARED -> {
                            binding.timeLeft.text = DEFAULT_TIME_LEFT
                        }
                        StatePlayer.STATE_DEFAULT -> {
                            binding.timeLeft.text = DEFAULT_TIME_LEFT
                        }
                    }
                }
            },
            DELAY
        )
    }

    override fun setupListener(listener: PlayerListener) {
        this.listener = listener
    }

}