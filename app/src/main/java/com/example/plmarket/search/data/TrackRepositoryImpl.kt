package com.example.plmarket.search.data

import com.example.plmarket.Resource
import com.example.plmarket.media.data.TrackDbConvertor
import com.example.plmarket.media.data.db.AppDatabase
import com.example.plmarket.player.domain.models.Track
import com.example.plmarket.search.data.dto.TrackSearchRequest
import com.example.plmarket.search.data.dto.TrackSearchResponse
import com.example.plmarket.search.domain.TrackRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class TrackRepositoryImpl(
    private val networkClient: NetworkClient,
    private val appDatabase: AppDatabase,
    private val trackDbConvertor: TrackDbConvertor,
) : TrackRepository {
    private var codeResult = 0

    override fun code(): Int {
        return codeResult
    }

    override fun searchTrack(expression: String): Flow<Resource<List<Track>>> = flow {
        val response = networkClient.doRequest(TrackSearchRequest(expression))

        when (response.resultCode) {
            -1 -> emit(Resource.Error(SearchStatus.NO_INTERNET.nameStatus))
            200 -> {
                val listFavoriteTrack: List<String> = appDatabase.trackDao().getTracksId()
                with(response as TrackSearchResponse) {
                    val data = results.map {
                        Track(
                            it.trackId,
                            it.trackName,
                            it.artistName,
                            it.trackTimeMillis,
                            it.artworkUrl100,
                            it.collectionName,
                            it.releaseDate,
                            it.primaryGenreName,
                            it.country,
                            it.previewUrl
                        )
                    }
                    checkFavoriteTrack(data, listFavoriteTrack)
                    emit(Resource.Success(data))
                }
            }
            else -> emit(Resource.Error(SearchStatus.NO_INTERNET.nameStatus))
        }
    }

    private fun checkFavoriteTrack(data: List<Track>, listFavoriteTrack: List<String>) {
        for (i in data) {
            for (k in listFavoriteTrack) {
                if (i.trackId == k) {
                    i.isFavorite = true
                }
            }
        }
    }
}