package com.example.plmarket.player.data

import com.example.plmarket.player.data.dto.TrackSearchRequest
import com.example.plmarket.player.data.dto.TrackSearchResponse
import com.example.plmarket.player.domain.api.TrackRepository
import com.example.plmarket.player.domain.models.Track

class PlayerRepositoryImpl(private val networkClient: NetworkClient) : TrackRepository {
    override fun searchTrack(expression: String): List<Track> {
        val response = networkClient.doRequest(TrackSearchRequest(expression))
        if (response.resultCode == 200) {
            return (response as TrackSearchResponse).results.map {
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
        }else {
            return emptyList()
        }
    }


}