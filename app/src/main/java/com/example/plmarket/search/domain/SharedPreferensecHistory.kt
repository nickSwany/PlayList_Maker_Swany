package com.example.plmarket.search.domain

import com.example.plmarket.player.domain.models.Track

interface SharedPreferensecHistory {
    fun addHistoryTracks(tracksHistory: ArrayList<Track>)

    fun aeditHistoryList(tracksHistory: ArrayList<Track>)

    fun clearTrack(tracksHistory: ArrayList<Track>)

    fun addTrackInAdapter(track: Track)
}