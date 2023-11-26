package com.example.plmarket.player.domain.api

import com.example.plmarket.player.domain.models.Track

interface TrackRepository {
    fun searchTrack(expression: String): List<Track>
}