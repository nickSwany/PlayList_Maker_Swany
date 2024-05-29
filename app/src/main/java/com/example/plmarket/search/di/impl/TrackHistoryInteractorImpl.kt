package com.example.plmarket.search.di.impl

import com.example.plmarket.player.domain.models.Track
import com.example.plmarket.search.domain.SharedPreferensecHistory
import com.example.plmarket.search.domain.TrackHistoryInteractor

class TrackHistoryInteractorImpl(private val sharedHistory: SharedPreferensecHistory) :
    TrackHistoryInteractor {

    override fun editHistoryList(tracksHistory: ArrayList<Track>) {
        sharedHistory.editHistoryList()
    }
    override fun clearTrack() {
    sharedHistory.clearTrack()
    }

    override fun getAllTrack(): List<Track> {
        return sharedHistory.getAllTrack()
    }

    override fun saveTrack(track: Track) {
        sharedHistory.saveTrack(track)
    }
}