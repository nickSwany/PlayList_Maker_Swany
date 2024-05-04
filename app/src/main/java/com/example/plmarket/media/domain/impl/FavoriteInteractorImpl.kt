package com.example.plmarket.media.domain.impl

import com.example.plmarket.media.data.repository.FavoriteRepository
import com.example.plmarket.media.domain.FavoriteListener
import com.example.plmarket.media.domain.db.FavoriteInteractor
import com.example.plmarket.player.domain.models.Track
import kotlinx.coroutines.flow.Flow

class FavoriteInteractorImpl(private val favoriteRepository: FavoriteRepository) :
    FavoriteInteractor {

    override fun favoriteTracks(): Flow<List<Track>> {
        return favoriteRepository.favoriteTrack()
    }

    override fun setListener(listener: FavoriteListener) {
        favoriteRepository.setupListener(listener)
    }

    override suspend fun addFavoriteTrack(track: Track) {
        favoriteRepository.addFavoriteTrack(track)
    }

    override suspend fun checkLikeTrack(trackId: String): Boolean {
        return favoriteRepository.checkLikeTrack(trackId)
    }
}