package com.example.plmarket.media.ui.adapter

import com.example.plmarket.player.domain.models.Track

interface TrackPlayListLongClickListener {

    fun onClick(track: Track)

    fun onLongClick(trackId: String)
}