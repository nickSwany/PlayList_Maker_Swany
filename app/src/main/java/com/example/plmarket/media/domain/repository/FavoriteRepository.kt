package com.example.plmarket.media.domain.repository

import com.example.plmarket.media.domain.FavoriteListener
import com.example.plmarket.player.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {

    fun favoriteTrack(): Flow<List<Track>>

    suspend fun addFavoriteTrack(track: Track)

    suspend fun deleteFavoriteTrack(trackId: String)

    fun setupListener(listener: FavoriteListener)

    suspend fun checkLikeTrack(trackId: String): Boolean

}