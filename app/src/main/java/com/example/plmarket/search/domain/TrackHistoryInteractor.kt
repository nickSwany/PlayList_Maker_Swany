package com.example.plmarket.search.domain

import com.example.plmarket.player.domain.models.Track

interface TrackHistoryInteractor {
    fun addHistoryTracks(tracksHistory: ArrayList<Track>)

    fun editHistoryList(tracksHistory: ArrayList<Track>)

    fun clearTrack(tracksHistory: ArrayList<Track>)

    fun addTrackInAdapter(track: Track)
}