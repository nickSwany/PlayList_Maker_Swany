package com.example.plmarket.media.ui

import com.example.plmarket.media.domain.module.PlayList

sealed interface CreateNewPlayListState{

    class CreatePlayList(
        val playList: PlayList
    ) : CreateNewPlayListState

    object NewPlayList: CreateNewPlayListState
}