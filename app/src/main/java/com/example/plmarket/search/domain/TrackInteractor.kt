package com.example.plmarket.search.domain

import com.example.plmarket.player.domain.models.Track

interface TrackInteractor {
    fun searchTrack(expression: String, consumer: TrackConsumer)

    interface TrackConsumer {
        fun consume(foundTrack: List<Track>?, errorMessage: String?)
    }
}