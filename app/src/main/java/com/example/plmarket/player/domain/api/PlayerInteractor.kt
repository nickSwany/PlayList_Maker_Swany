package com.example.plmarket.player.domain.api


interface PlayerInteractor {
    fun playbackControl()
    fun startPlayer()
    fun pausePlayer()
    fun preparePlayer(url: String)
    fun releasePlayer()
    fun setListener(listener: PlayerListener)
    fun getTime() : String
}