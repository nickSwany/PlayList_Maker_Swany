package com.example.pl_market.data.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import search.SearchResponse

interface ApiService {
    @GET("/search")
    fun searchTracks(@Query("term") text: String): Call<SearchResponse>
}