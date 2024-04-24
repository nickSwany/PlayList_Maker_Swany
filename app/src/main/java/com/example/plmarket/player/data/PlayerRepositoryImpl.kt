package com.example.plmarket.player.data

import android.media.MediaPlayer
import com.example.plmarket.player.domain.StatePlayer
import com.example.plmarket.player.domain.api.PlayerListener
import com.example.plmarket.player.domain.api.PlayerRepository
import java.text.SimpleDateFormat
import java.util.*

class PlayerRepositoryImpl : PlayerRepository {
    companion object {
        private const val DEFAULT_TIME_LEFT = "00:00"
        private const val DELAY = 1000L
    }

    private var listener: PlayerListener? = null
    private var playerState = StatePlayer.STATE_DEFAULT
    private var time = DEFAULT_TIME_LEFT

    //    val handler = Handler(Looper.getMainLooper())
    private val mediaPlayer = MediaPlayer()

    override fun preparePlayer(url: String) {
        try {
            mediaPlayer.reset()
            mediaPlayer.setDataSource(url)
            mediaPlayer.prepareAsync()

            mediaPlayer.setOnPreparedListener {
                playerState = StatePlayer.STATE_PREPARED
                listener?.onStateUpdate(playerState)
            }

            mediaPlayer.setOnCompletionListener {
                playerState = StatePlayer.STATE_PREPARED
                listener?.onStateUpdate(playerState)
                time = DEFAULT_TIME_LEFT
            }
        } catch (e: Exception) {
            e.printStackTrace()
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
            else -> {
                StatePlayer.STATE_DEFAULT
            }
        }
    }

    override fun startPlayer() {
        playerState = StatePlayer.STATE_PLAYING
        mediaPlayer.start()
//        updateTime(time)
        listener?.onStateUpdate(playerState)
    }

    override fun pausePlayer() {
        playerState = StatePlayer.STATE_PAUSED
        mediaPlayer.pause()
        listener?.onStateUpdate(playerState)
    }

//    override fun updateTime(time: String) {
//        this.time = time
//
//        handler.postDelayed(
//            object : Runnable {
//                override fun run() {
//                    if (playerState == StatePlayer.STATE_PLAYING) {
//                        this@PlayerRepositoryImpl.time = SimpleDateFormat(
//                            "mm:ss",
//                            Locale.getDefault()
//                        ).format(mediaPlayer.currentPosition)
//                        listener?.onTimeUpdate(this@PlayerRepositoryImpl.time)
//                        handler.postDelayed(
//                            this,
//                            DELAY,
//                        )
//                    }
//                }
//            },
//            DELAY
//        )
//    }

    override fun setupListener(listener: PlayerListener) {
        this.listener = listener
    }

    override fun getTime(): String {
        time = SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition)
            ?: DEFAULT_TIME_LEFT
        return time
    }
}