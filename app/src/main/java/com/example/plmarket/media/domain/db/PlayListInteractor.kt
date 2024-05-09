package com.example.plmarket.media.domain.db

import android.net.Uri
import com.example.plmarket.media.domain.module.PlayList
import com.example.plmarket.player.domain.models.Track
import kotlinx.coroutines.flow.Flow


interface PlayListInteractor {

    suspend fun deletePlayList(playListId: Int): Boolean

    suspend fun deleteTracksPlayList(trackId: String, playListId: Int): Boolean

    suspend fun addPlayList(name: String, descriptiom: String, uri: String)

    suspend fun addTrackPlayList(track: Track, playList: PlayList): Boolean

    suspend fun getTracksForPlayList(playListId: Int): Flow<List<Track>>

    suspend fun getTracksForPlayListCount(playList: PlayList): Int

    suspend fun getPlayList(id: Int): PlayList

    suspend fun updatePlayList(name: String, description: String, uri: String, id: Int)

    fun getPlayList(): Flow<List<PlayList>>

    fun saveImageToPrivateStorage(uri: Uri)

    fun getUri(uriPlayList: String): String
}