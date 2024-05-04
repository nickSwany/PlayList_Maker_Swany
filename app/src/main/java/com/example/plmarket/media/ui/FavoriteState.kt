package com.example.plmarket.media.ui

import com.example.plmarket.player.domain.models.Track

sealed interface FavoriteState {

    object Loading: FavoriteState

    object Empty : FavoriteState

    data class Content(
        val track : List<Track>
    ) : FavoriteState
}