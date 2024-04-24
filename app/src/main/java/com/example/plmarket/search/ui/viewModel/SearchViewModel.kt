package com.example.plmarket.search.ui.viewModel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plmarket.player.domain.models.Track
import com.example.plmarket.search.domain.TrackHistoryInteractor
import com.example.plmarket.search.domain.TrackInteractor
import com.example.plmarket.search.ui.fragment.TrackState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchViewModel(
    private val interactorHistory: TrackHistoryInteractor,
    private val interactorSearch: TrackInteractor
) : ViewModel() {

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }

    private var latestSearchText: String? = null
    private var searchJob: Job? = null

    private val _clearHistory = MutableLiveData<Unit>()
    val clearHistory: LiveData<Unit> = _clearHistory

    fun addHistoryTracks(tracksHistory: ArrayList<Track>) {
        interactorHistory.addHistoryTracks(tracksHistory)
    }

    fun addHistoryList(tracksHistory: ArrayList<Track>) {
        interactorHistory.editHistoryList(tracksHistory)
    }

    fun clearTrackListHistory(tracksHistory: ArrayList<Track>) {
        _clearHistory.value = interactorHistory.clearTrack(tracksHistory)
    }

    fun addHistoryTrack(track: Track) {
        interactorHistory.addTrackInAdapter(track)
    }

    private val stateLiveData = MutableLiveData<TrackState>()

    fun observeState(): LiveData<TrackState> = stateLiveData

    private fun renderState(state: TrackState) {
        stateLiveData.postValue(state)
    }

//    override fun onCleared() {
//        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
//    }

    fun searchDebounce(changedText: String) {
        if (latestSearchText == changedText) {
            return
        }

        latestSearchText = changedText

        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_DELAY)
            searchRequest(changedText)
        }
    }

    fun searchRequest(newSearchText: String) {
        if (newSearchText.isNotEmpty()) {

            renderState(TrackState.Loading)

            viewModelScope.launch {
                interactorSearch
                    .searchTrack(newSearchText)
                    .collect { pair ->
                        processResult(pair.first, pair.second)
                    }
            }
        }
    }

    private fun processResult(foundTrack: List<Track>?, errorMessage: String?) {
        val tracks = mutableListOf<Track>()
        if (foundTrack != null) {
            tracks.addAll(foundTrack)
        }
        when {
            errorMessage != null -> {
                renderState(TrackState.Error)
            }
            tracks.isEmpty() -> {
                renderState(TrackState.Empty)
            }
            else -> {
                renderState(TrackState.Content(tracks = tracks))
            }
        }
    }
}