package com.example.plmarket.media.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plmarket.media.domain.db.PlayListInteractor
import com.example.plmarket.media.domain.module.PlayList
import com.example.plmarket.media.ui.PlayListStateTrack
import com.example.plmarket.player.domain.models.Track
import kotlinx.coroutines.launch

class PlayListReviewViewModel(val interactor: PlayListInteractor) : ViewModel() {

    private val playList = MutableLiveData<PlayList>()
    fun observePlayList(): LiveData<PlayList> = playList

    private val deleteTrackState = MutableLiveData<Boolean>()
    fun observeStateDeleteTrack(): LiveData<Boolean> = deleteTrackState

    private val deletePlayListState = MutableLiveData<Boolean>()
    fun observeStateDeletePlayList(): LiveData<Boolean> = deletePlayListState

    private val statePlayListTracksReview = MutableLiveData<PlayListStateTrack>()
    fun observeStateTrackReview(): LiveData<PlayListStateTrack> = statePlayListTracksReview

    fun showInfoPlayList(playListId: Int) {
        viewModelScope.launch {
            playList.postValue(interactor.getPlayList(playListId))
        }
    }

    fun fillDataTracks(playListId: Int) {
        viewModelScope.launch {
            interactor.getTrackForPlayList(playListId).collect { tracks ->
                if (tracks.isEmpty()) {
                    statePlayListTracksReview.postValue(PlayListStateTrack.Empty)
                } else statePlayListTracksReview.postValue(PlayListStateTrack.Content(tracks))
            }
        }
    }

    fun deleteTrackInPlayList(trackId: String, playListId: Int) {
        viewModelScope.launch {
            deleteTrackState.postValue(interactor.deleteTrackPlayList(trackId, playListId))
        }
    }

    fun deletePlayList(playListId: Int) {
        viewModelScope.launch {
            deletePlayListState.postValue(interactor.deletePlayList(playListId))
        }
    }

    fun sharePlayList(
        tracks: List<Track>,
        nameTrack: String,
        description: String,
        currentTrack: String
    ) {
        interactor.sharePlayList(tracks, nameTrack, description, currentTrack)
    }
}