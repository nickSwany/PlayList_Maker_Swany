package com.example.pl_market.data.dto

class TrackSearchResponse(
    val searchType: String,
    val expression: String,
    val results: List<TrackDto>
) : Response() {
}