package com.example.plmarket.search

import com.example.plmarket.player.domain.models.Track
import com.google.gson.annotations.SerializedName

class SearchResponse(
    @SerializedName("resultCount") val resultCount: Int,
    @SerializedName("results") val results: List<Track>
)