package com.example.plmarket.media.domain.module

data class PlayList(
    val playListId: Int,
    val name: String,
    val description: String?,
    val uri: String?,
    var currentTracks: Int = 0,
)