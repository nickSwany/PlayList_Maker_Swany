package com.example.plmarket.media.ui

import com.example.plmarket.player.domain.models.Track

sealed interface PlayListStateTrack {
    object Empty : PlayListStateTrack

    class Content(val tracks : List<Track>) : PlayListStateTrack
}