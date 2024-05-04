package com.example.plmarket.media.domain.db

import com.example.plmarket.media.domain.FavoriteListener
import com.example.plmarket.player.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteInteractor {

    fun favoriteTracks(): Flow<List<Track>>

    fun setListener(listener: FavoriteListener)

    suspend fun addFavoriteTrack(track: Track)

    suspend fun checkLikeTrack(trackId: String): Boolean
}