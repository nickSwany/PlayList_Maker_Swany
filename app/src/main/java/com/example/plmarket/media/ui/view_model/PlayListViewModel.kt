package com.example.plmarket.media.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plmarket.media.domain.db.PlayListInteractor
import com.example.plmarket.media.domain.module.PlayList
import com.example.plmarket.media.ui.PlayListState
import kotlinx.coroutines.launch

class PlayListViewModel(private val interactor: PlayListInteractor) : ViewModel() {

    private val statePlayList = MutableLiveData<PlayListState>()
    fun observeState(): LiveData<PlayListState> = statePlayList

    fun fillData() {
        statePlayList.postValue(PlayListState.Loading)

        viewModelScope.launch {
            interactor.getPlayList().collect() {
                processResult(it)
            }
        }
    }

    private fun processResult(playList: List<PlayList>) {
        if (playList.isEmpty()) {
            statePlayList.postValue(PlayListState.Empty)
        } else {
            statePlayList.postValue(PlayListState.Content(playList))
        }
    }
}