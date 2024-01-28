package com.example.plmarket.search.domain

import com.example.plmarket.Resource
import com.example.plmarket.player.domain.models.Track

interface TrackRepository {
    fun code() : Int
    fun searchTrack(expression: String) : Resource<List<Track>>
}