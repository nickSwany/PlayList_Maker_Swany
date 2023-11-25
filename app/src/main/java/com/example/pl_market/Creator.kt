package com.example.pl_market

import com.example.pl_market.data.PlayerRepositoryImpl
import com.example.pl_market.data.network.RetrofitNetworkClient
import com.example.pl_market.domain.api.TrackInteractor
import com.example.pl_market.domain.api.TrackRepository
import com.example.pl_market.domain.impl.TracksInteractorImpl
import retrofit2.http.GET

object Creator {
    private fun getTrackRepository() : TrackRepository {
        return PlayerRepositoryImpl(RetrofitNetworkClient())
    }
    fun provideTracksInteractor() : TrackInteractor {
        return TracksInteractorImpl(getTrackRepository())
    }
}