package com.example.plmarket.search.data.dto

import com.example.plmarket.Resource
import com.example.plmarket.search.data.NetworkClient
import com.example.plmarket.player.domain.models.Track
import com.example.plmarket.search.data.SearchStatus
import com.example.plmarket.search.domain.TrackRepository


class TrackRepositoryImpl(private val networkClient: NetworkClient) : TrackRepository {
    private var codeResult = 0

    override fun code(): Int {
        return codeResult
    }

    override fun searchTrack(expression: String): Resource<List<Track>> {
        val response = networkClient.doRequest(TrackSearchRequest(expression))


        return when (response.resultCode) {
            -1 -> Resource.Error(SearchStatus.NO_INTERNET.nameStatus)
            200 -> {
                Resource.Success((response as TrackSearchResponse).results.map {
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
                })
            }
            else -> Resource.Error(SearchStatus.NO_INTERNET.nameStatus)
        }
    }
}