package com.example.plmarket.player.domain.api


interface PlayerRepository {
    fun playbackControl()
    fun startPlayer()
    fun pausePlayer()
    fun preparePlayer(url: String)
    fun releasePlayer()
//    fun updateTime(time: String)
    fun setupListener(listener: PlayerListener)
    fun getTime(): String
}