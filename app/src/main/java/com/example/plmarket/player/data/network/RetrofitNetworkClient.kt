package com.example.plmarket.player.data.network

import com.example.plmarket.player.data.NetworkClient
import com.example.plmarket.player.data.dto.Response
import com.example.plmarket.player.data.dto.TrackSearchRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient : NetworkClient {
    companion object {
        const val iTunesBaseUrl = "https://itunes.apple.com"
    }
    private val retrofit = Retrofit.Builder()
        .baseUrl(iTunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val apiService = retrofit.create(ApiService::class.java)

    override fun doRequest(dto: Any): Response {
        if (dto is TrackSearchRequest) {
            val resp = apiService.searchTracks(dto.expression).execute()
            return Response(resultCode = resp.code())
        } else {
            return Response(resultCode = 400)
        }
    }
}
