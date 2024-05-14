package com.example.plmarket.media.domain.repository

import com.example.plmarket.media.domain.module.PlayList
import com.example.plmarket.player.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlayListRepository {

    fun getPlayList(): Flow<List<PlayList>>

    suspend fun addPlayList(name: String, description: String, uri: String)

    suspend fun addTrackPlayList(track: Track, playList: PlayList): Boolean

    suspend fun getPlayList(id: Int): PlayList

    suspend fun updatePlayList(name: String, description: String, uri: String, id: Int)
}