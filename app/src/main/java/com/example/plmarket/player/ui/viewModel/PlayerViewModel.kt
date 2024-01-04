package com.example.plmarket.player.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.plmarket.player.domain.StatePlayer
import com.example.plmarket.player.domain.api.PlayerInteractor
import com.example.plmarket.player.domain.api.PlayerListener
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel(private val playerInteractor: PlayerInteractor) : ViewModel(),
    PlayerListener {

    init {
        listen()
    }

    private val _secondCounter = MutableLiveData<String>()
    val secondCounter: LiveData<String> = _secondCounter

    private val _checkState = MutableLiveData<StatePlayer>()
    val checkState: LiveData<StatePlayer> = _checkState

    private val _timeSomg = MutableLiveData<String>()
    val timeSong: LiveData<String> = _timeSomg

    private val _dataSong = MutableLiveData<String>()
    val dataSong: LiveData<String> = _dataSong

    private val _coverArtwork = MutableLiveData<String>()
    val coverArtwork: LiveData<String> = _coverArtwork

    fun getCoverArtwork(artworkUrl100: String?) {
        _coverArtwork.value = artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg")
    }

    fun correctDataSong(data: String) {
        _dataSong.value = data.substring(0, 4)
    }

    fun correctTimeSong(time: String?) {
        _timeSomg.value = SimpleDateFormat("mm:ss", Locale.getDefault()).format(time?.toInt())
    }

    fun preparePlayer(urlTrack: String) {
        playerInteractor.preparePlayer(urlTrack)
    }

    fun playStart() {
        playerInteractor.playbackControl()
    }

    fun onPause() {
        playerInteractor.pausePlayer()
    }

    override fun onCleared() {
        super.onCleared()
        playerInteractor.releasePlayer()
    }

    private fun listen() {
        playerInteractor.setListener(this)
    }

    override fun onStateUpdate(state: StatePlayer) {
        _checkState.value = state
    }

    override fun onTimeUpdate(time: String) {
        _secondCounter.value = time
    }
}