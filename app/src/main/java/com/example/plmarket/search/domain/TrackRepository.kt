package com.example.plmarket.search.domain

import com.bumptech.glide.load.engine.Resource
import com.example.plmarket.player.domain.models.Track

interface TrackRepository {
    fun code() : Int
    fun searchTrack(expression: String) : com.example.plmarket.Resource<List<Track>>
}