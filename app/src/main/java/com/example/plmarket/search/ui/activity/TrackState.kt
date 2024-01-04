package com.example.plmarket.search.ui.activity

import com.example.plmarket.player.domain.models.Track

sealed class TrackState{
    object Loading: TrackState()

    data class Content(val tracks: List<Track>) : TrackState()

    object Error: TrackState()

    object Empty: TrackState()
}
