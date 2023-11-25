package com.example.pl_market.domain.api

import com.example.pl_market.domain.models.Track

interface TrackInteractor {
    fun searchTrack(expression: String, consumer: TrackConsumer)
    interface TrackConsumer {
        fun consume (foundTracks: List<Track>)
    }
}