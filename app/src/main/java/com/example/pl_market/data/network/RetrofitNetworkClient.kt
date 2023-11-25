// file: com.example.pl_market.data.network.RetrofitNetworkClient.kt

package com.example.pl_market.data.network

import com.example.pl_market.data.NetworkClient
import com.example.pl_market.data.dto.Response
import com.example.pl_market.data.dto.TrackSearchRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient(override var resultCode: Int) : NetworkClient {
    private val iTunesBaseUrl = "https://itunes.apple.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(iTunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val apiService = retrofit.create(ApiService::class.java)

    override fun doRequest(dto: Any): Response {
        if (dto is TrackSearchRequest) {
            val resp = apiService.searchTracks(dto.expression).execute()

            val body = resp.body() ?: Response()

            return body.apply {
                resultCode = resp.code()
            } as Response
        } else {
            return Response().apply { resultCode = 400 }
        }
    }
}
