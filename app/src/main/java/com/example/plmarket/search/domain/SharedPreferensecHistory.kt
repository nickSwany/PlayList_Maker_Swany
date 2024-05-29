package com.example.plmarket.search.domain

import com.example.plmarket.player.domain.models.Track

interface SharedPreferensecHistory {
    fun saveTrack(track: Track)

    fun getAllTrack(): List<Track>

    fun clearTrack()

    fun editHistoryList()
}