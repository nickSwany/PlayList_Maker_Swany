package com.example.plmarket.search.data.dto

import com.example.plmarket.Resource
import com.example.plmarket.search.data.NetworkClient
import com.example.plmarket.player.domain.models.Track
import com.example.plmarket.search.data.SearchStatus
import com.example.plmarket.search.domain.TrackRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class TrackRepositoryImpl(private val networkClient: NetworkClient) : TrackRepository {
    private var codeResult = 0

    override fun code(): Int {
        return codeResult
    }

    override fun searchTrack(expression: String): Flow<Resource<List<Track>>> = flow {
        val response = networkClient.doRequest(TrackSearchRequest(expression))

        when (response.resultCode) {
            -1 -> emit(Resource.Error(SearchStatus.NO_INTERNET.nameStatus))
            200 -> {
                with(response as TrackSearchResponse){
                    val data = results.map {
                        Track(
                            it.trackName,
                            it.artistName,
                            it.trackTimeMillis,
                            it.artworkUrl100,
                            it.trackId,
                            it.collectionName,
                            it.releaseDate,
                            it.primaryGenreName,
                            it.country,
                            it.previewUrl
                        )
                    }
                    emit(Resource.Success(data))
                }
            }
            else -> emit(Resource.Error(SearchStatus.NO_INTERNET.nameStatus))
        }
    }
}