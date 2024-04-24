package com.example.plmarket.search.data.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import com.example.plmarket.search.data.dto.TrackSearchResponse

interface ApiService {
    @GET("/search?entity=song")
    suspend fun searchTracks(@Query("term") text: String): TrackSearchResponse
}