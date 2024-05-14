package com.example.plmarket.media.domain.impl

import android.net.Uri
import com.example.plmarket.media.domain.db.PlayListInteractor
import com.example.plmarket.media.domain.module.PlayList
import com.example.plmarket.media.domain.repository.AlbumPictureRepository
import com.example.plmarket.media.domain.repository.PlayListRepository
import com.example.plmarket.player.domain.models.Track
import kotlinx.coroutines.flow.Flow

class PlayListInteractorImpl(
    private val playListRepository: PlayListRepository,
    private val albumPictureRepository: AlbumPictureRepository
) : PlayListInteractor {

    override suspend fun addPlayList(name: String, description: String, uri: String) {
        playListRepository.addPlayList(name, description, uri)
    }

    override suspend fun addTrackPlayList(track: Track, playList: PlayList): Boolean {
        return playListRepository.addTrackPlayList(track, playList)
    }

    override suspend fun getPlayList(id: Int): PlayList {
        return playListRepository.getPlayList(id)
    }

    override suspend fun updatePlayList(name: String, description: String, uri: String, id: Int) {
        playListRepository.updatePlayList(name, description, uri, id)
    }

    override fun getPlayList(): Flow<List<PlayList>> {
        return playListRepository.getPlayList()
    }

    override fun saveImageToPrivateStorage(uri: Uri) {
        albumPictureRepository.saveImageToPrivateStorage(uri)
    }

    override fun getUri(uriPlayList: String): String {
        return albumPictureRepository.getUri(uriPlayList)
    }
}