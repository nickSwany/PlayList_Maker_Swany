package com.example.plmarket.media.ui.adapter

import com.example.plmarket.media.domain.module.PlayList

fun interface PlayListClickListener {

    fun onPlayListClick(playList: PlayList)
}