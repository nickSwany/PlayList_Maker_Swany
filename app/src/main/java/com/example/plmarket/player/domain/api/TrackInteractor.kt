package com.example.plmarket.player.domain.api

import com.example.plmarket.player.domain.models.Track

interface TrackInteractor {
    fun searchTrack(expression: String, consumer: TrackConsumer)
    interface TrackConsumer {
        fun consume (foundTracks: List<Track>)
    }
}