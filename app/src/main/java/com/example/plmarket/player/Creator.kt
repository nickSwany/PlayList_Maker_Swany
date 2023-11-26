package com.example.plmarket.player

import com.example.plmarket.player.data.PlayerRepositoryImpl
import com.example.plmarket.player.data.network.RetrofitNetworkClient
import com.example.plmarket.player.domain.api.TrackInteractor
import com.example.plmarket.player.domain.api.TrackRepository
import com.example.plmarket.player.domain.impl.TracksInteractorImpl

object Creator {
    private fun getTrackRepository() : TrackRepository {
        return PlayerRepositoryImpl(RetrofitNetworkClient())
    }
    fun provideTracksInteractor() : TrackInteractor {
        return TracksInteractorImpl(getTrackRepository())
    }
}