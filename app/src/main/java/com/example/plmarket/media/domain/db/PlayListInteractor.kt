package com.example.plmarket.media.domain.db

import android.net.Uri
import com.example.plmarket.media.domain.module.PlayList
import com.example.plmarket.player.domain.models.Track
import kotlinx.coroutines.flow.Flow


interface PlayListInteractor {

    suspend fun addPlayList(name: String, description: String, uri: String)

    suspend fun addTrackPlayList(track: Track, playList: PlayList): Boolean

    suspend fun getPlayList(id: Int): PlayList

    suspend fun updatePlayList(name: String, description: String, uri: String, id: Int)

    fun getPlayList(): Flow<List<PlayList>>

    fun saveImageToPrivateStorage(uri: Uri)

    fun getUri(uriPlayList: String): String
}