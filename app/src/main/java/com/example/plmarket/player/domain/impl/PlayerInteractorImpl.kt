package com.example.plmarket.player.domain.impl

import com.example.plmarket.player.domain.api.PlayerInteractor
import com.example.plmarket.player.domain.api.PlayerListener
import com.example.plmarket.player.domain.api.PlayerRepository

class PlayerInteractorImpl(private var repository: PlayerRepository) : PlayerInteractor {
    override fun playbackControl() {
        repository.playbackControl()
    }

    override fun pausePlayer() {
        repository.pausePlayer()
    }

    override fun startPlayer() {
        repository.startPlayer()
    }

    override fun releasePlayer() {
        repository.releasePlayer()
    }

    override fun preparePlayer(trackUrl: String) {
        repository.preparePlayer(trackUrl)
    }

    override fun setListener(listener: PlayerListener) {
        repository.setupListener(listener)
    }
}