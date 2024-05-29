package com.example.plmarket.media.data.repository


import com.example.plmarket.media.data.TrackDbConvertor
import com.example.plmarket.media.data.db.AppDatabase
import com.example.plmarket.media.data.db.entity.TrackEntity
import com.example.plmarket.media.domain.repository.FavoriteRepository
import com.example.plmarket.media.domain.FavoriteListener
import com.example.plmarket.player.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoriteRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val trackDbConvertor: TrackDbConvertor,
) : FavoriteRepository {

    private var listener: FavoriteListener? = null

    override fun favoriteTrack(): Flow<List<Track>> = flow {
        val tracks = appDatabase.trackDao().getTracks()
        emit(convertFromTrackEntity(tracks))
    }

    override suspend fun addFavoriteTrack(track: Track) {
        if (track.isFavorite) {
            track.isFavorite = false
            appDatabase.trackDao().deleteTrackEntity(track.trackId)
            listener?.onFavoriteUpdate(track.isFavorite)
        } else {
            track.isFavorite = true
            appDatabase.trackDao().insertTracks(tracks = trackConvertorEntity(track))
            listener?.onFavoriteUpdate(track.isFavorite)
        }
    }

    override suspend fun deleteFavoriteTrack(trackId: String) {
        appDatabase.trackDao().deleteTrackEntity(trackId)
    }


    private fun convertFromTrackEntity(tracks: List<TrackEntity>): List<Track> {
        return tracks.map { track -> trackDbConvertor.map(track) }
    }

    private fun trackConvertorEntity(track: Track): TrackEntity {
        return trackDbConvertor.map(track)
    }

    override suspend fun checkLikeTrack(trackId: String): Boolean {
        return appDatabase.trackDao().doesTrackExist(trackId)
    }

    override fun setupListener(listener: FavoriteListener) {
        this.listener = listener
    }
}