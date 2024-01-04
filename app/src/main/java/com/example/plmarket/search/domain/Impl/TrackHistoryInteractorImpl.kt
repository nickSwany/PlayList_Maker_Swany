package com.example.plmarket.search.domain.Impl

import com.example.plmarket.player.domain.models.Track
import com.example.plmarket.search.domain.SharedPreferensecHistory
import com.example.plmarket.search.domain.TrackHistoryInteractor

class TrackHistoryInteractorImpl(private val sharedHistory: SharedPreferensecHistory) :
    TrackHistoryInteractor {
    override fun addHistoryTracks(tracksHistory: ArrayList<Track>) {
        sharedHistory.addHistoryTracks(tracksHistory)
    }

    override fun addTrackInAdapter(track: Track) {
        sharedHistory.addTrackInAdapter(track)

    }

    override fun additHistoryList(tracksHistory: ArrayList<Track>) {
        sharedHistory.aeditHistoryList(tracksHistory)
    }

    override fun clearTrack(tracksHistory: ArrayList<Track>) {
        sharedHistory.clearTrack(tracksHistory)
    }
}