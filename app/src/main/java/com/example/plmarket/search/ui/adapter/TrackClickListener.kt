package com.example.plmarket.search.ui.adapter

import com.example.plmarket.player.domain.models.Track

fun interface TrackClickListener {
    fun onTrackClick(track: Track)
}