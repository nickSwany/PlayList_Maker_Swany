package com.example.plmarket.media.ui.adapter

import com.example.plmarket.media.domain.module.PlayList

interface PlayListClickListener {

    fun onPlayListClick(playList: PlayList)
}