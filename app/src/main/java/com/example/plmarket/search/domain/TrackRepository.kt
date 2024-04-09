package com.example.plmarket.search.domain

import com.example.plmarket.Resource
import com.example.plmarket.player.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface TrackRepository {
    fun code(): Int
    fun searchTrack(expression: String): Flow<Resource<List<Track>>>
}