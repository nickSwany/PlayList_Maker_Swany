package com.example.pl_market.domain.api

import com.example.pl_market.domain.models.Track

interface TrackRepository {
    fun searchTrack(expression: String): List<Track>
}