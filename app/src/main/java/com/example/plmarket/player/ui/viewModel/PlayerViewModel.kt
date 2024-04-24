package com.example.plmarket.player.ui.viewModel

import android.media.MediaPlayer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plmarket.player.domain.StatePlayer
import com.example.plmarket.player.domain.api.PlayerInteractor
import com.example.plmarket.player.domain.api.PlayerListener
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel(private val playerInteractor: PlayerInteractor) : ViewModel(),
    PlayerListener {

    companion object {
        const val DEFAULT_TIME_FOR_TRACK = "00:00"
        const val UPDATE_STATUS_PLAYER = 300L
    }

    init {
        listen()
    }

    private var timerJob: Job? = null

    private val _secondCounter = MutableLiveData<String>()
    val secondCounter: LiveData<String> = _secondCounter

    private val _checkState = MutableLiveData<StatePlayer>()
    val checkState: LiveData<StatePlayer> = _checkState

    private val _timeSong = MutableLiveData<String>()
    val timeSong: LiveData<String> = _timeSong

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
        _timeSong.value = SimpleDateFormat("mm:ss", Locale.getDefault()).format(time?.toInt())
    }

    fun preparePlayer(urlTrack: String) {
        playerInteractor.preparePlayer(urlTrack)
    }

    fun playStart() {
        playerInteractor.playbackControl()
        startTimer()
    }

    fun onPause() {
        playerInteractor.pausePlayer()
        timerJob?.cancel()
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

//    override fun onTimeUpdate(time: String) {
//        _secondCounter.value = time
//    }

    private fun startTimer() {
        /*
        Можно, конечно, было оставить название onTimeUpdate, но решил взять другое.
        Пришлось практически всю логику вынести сюда, в репозитории не работали корутины.
        Если использовать PlayerState, то в репозитори в методе preparePlayer будут появляться ошибки, как их исправить не разобрался
        Поэтому пришлось оставить старый класс StatePlayer и уже работать с ним
         */
        timerJob = viewModelScope.launch {
            while(_checkState.value == StatePlayer.STATE_PLAYING){
                delay(UPDATE_STATUS_PLAYER)
                _secondCounter.value = playerInteractor.getTime()
            }
            if(_checkState.value == StatePlayer.STATE_PREPARED) {
                _secondCounter.value = DEFAULT_TIME_FOR_TRACK
            }
        }
    }
}