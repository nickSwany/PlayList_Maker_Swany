package com.example.plmarket.search.data.dto

import com.example.plmarket.search.data.dto.Response
import com.example.plmarket.search.data.dto.TrackDto

class TrackSearchResponse(
    val results: List<TrackDto>
) : Response()