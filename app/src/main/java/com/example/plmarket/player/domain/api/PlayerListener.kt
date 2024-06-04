package com.example.plmarket.player.domain.api

import com.example.plmarket.player.domain.StatePlayer

interface PlayerListener {

    fun onStateUpdate(state: StatePlayer)
}