package com.example.plmarket.search.domain

import com.example.plmarket.player.domain.models.Track

interface TrackHistoryInteractor {
    fun editHistoryList(tracksHistory: ArrayList<Track>)

    fun clearTrack()

    fun getAllTrack() : List<Track>

    fun saveTrack(track: Track)
}