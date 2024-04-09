package com.example.plmarket.search.domain

import com.example.plmarket.player.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface TrackInteractor {
    fun searchTrack(expression: String): Flow<Pair<List<Track>?, String?>>
}