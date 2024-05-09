package com.example.plmarket.media.ui.adapter

import com.example.plmarket.player.domain.models.Track

interface TrackClickListener {

    fun onTrackClick(track: Track)

    fun onItemClick(trackId: String)
}