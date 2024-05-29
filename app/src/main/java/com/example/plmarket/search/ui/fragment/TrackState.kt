package com.example.plmarket.search.ui.fragment

import com.example.plmarket.player.domain.models.Track

sealed interface TrackState {
    object Loading : TrackState

    data class Content(val tracks: List<Track>) : TrackState

    object Error : TrackState

    object Empty : TrackState

    object Default : TrackState

    data class SearchHistory(val tracks: List<Track>) : TrackState
}