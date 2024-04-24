package com.example.plmarket.player.ui.viewModel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plmarket.media.domain.FavoriteListener
import com.example.plmarket.media.domain.db.FavoriteInteractor
import com.example.plmarket.player.domain.StatePlayer
import com.example.plmarket.player.domain.api.PlayerInteractor
import com.example.plmarket.player.domain.api.PlayerListener
import com.example.plmarket.player.domain.models.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel(
    private val playerInteractor: PlayerInteractor,
    private val favoriteInteractor: FavoriteInteractor
) : ViewModel(),
    PlayerListener, FavoriteListener {

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

    private val _likeState = MutableLiveData<Boolean>()
    val likeState: LiveData<Boolean> = _likeState

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
        favoriteInteractor.setListener(this)
    }

    override fun onStateUpdate(state: StatePlayer) {
        _checkState.value = state
    }

    private fun startTimer() {
        timerJob = viewModelScope.launch {
            while (_checkState.value == StatePlayer.STATE_PLAYING) {
                delay(UPDATE_STATUS_PLAYER)
                _secondCounter.value = playerInteractor.getTime()
            }
            if (_checkState.value == StatePlayer.STATE_PREPARED) {
                _secondCounter.value = DEFAULT_TIME_FOR_TRACK
            }
        }
    }

    suspend fun addFavoriteTrack(track: Track) {
        favoriteInteractor.addFavoriteTrack(track)
    }

    fun checkLike(trackId: String) {
        viewModelScope.launch {
            _likeState.value = favoriteInteractor.checkLikeTrack(trackId)
        }
    }

    override fun onFavoriteUpdate(isLiked: Boolean) {
        _likeState.value = isLiked
    }
}