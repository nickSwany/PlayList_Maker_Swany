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
    private var historyJob: Job? = null

    private val _clearHistory = MutableLiveData<Unit>()
    val clearHistory: LiveData<Unit> = _clearHistory

    fun saveTrack(track: Track) {
        interactorHistory.saveTrack(track)
    }

    fun clearTrackListHistory() {
        interactorHistory.clearTrack()
        renderState(TrackState.SearchHistory(emptyList()))
    }

    private fun getHistoryTrack() = interactorHistory.getAllTrack()

    private val stateLiveData = MutableLiveData<TrackState>()

    fun observeState(): LiveData<TrackState> = stateLiveData

    private fun renderState(state: TrackState) {
        stateLiveData.postValue(state)
    }

    fun searchDebounce(changedText: String) {
        if (changedText.isBlank()) {
            historyJob = viewModelScope.launch {
                stateLiveData.value = TrackState.SearchHistory(getHistoryTrack())
            }
            searchJob?.cancel()
        } else {
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
    }

    fun clear() {
        renderState(TrackState.Default)
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