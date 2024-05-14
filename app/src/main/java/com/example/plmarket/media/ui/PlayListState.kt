package com.example.plmarket.media.ui

import com.example.plmarket.media.domain.module.PlayList

sealed interface PlayListState {
    object Empty : PlayListState

    object Loading : PlayListState

    data class Content(
        val playList: List<PlayList>
    ) : PlayListState
}