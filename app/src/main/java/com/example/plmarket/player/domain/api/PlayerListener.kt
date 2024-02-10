package com.example.plmarket.player.domain.api

import com.example.plmarket.player.domain.StatePlayer

interface PlayerListener {
    fun onTimeUpdate(time: String)
    fun onStateUpdate(state: StatePlayer)
}