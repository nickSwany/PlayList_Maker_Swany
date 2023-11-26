package com.example.plmarket.player.data.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import com.example.plmarket.search.SearchResponse

interface ApiService {
    @GET("/com/example/plmarket/search")
    fun searchTracks(@Query("term") text: String): Call<SearchResponse>
}