package com.example.plmarket.search.data.network

import com.example.plmarket.search.data.NetworkClient
import com.example.plmarket.search.data.dto.Response
import com.example.plmarket.search.data.dto.TrackSearchRequest
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

        return if (dto is TrackSearchRequest) {
            val resp = apiService.searchTracks(dto.expression).execute()
            val body = resp.body() ?: Response()
            body.apply { resultCode = resp.code() }
        } else {
            Response().apply { resultCode = 400 }
        }
    }
}
