package com.example.plmarket.media.data.repository

import android.content.Context
import com.example.plmarket.media.data.PlayListConvertor
import com.example.plmarket.media.data.db.AppDatabase
import com.example.plmarket.media.data.db.entity.PlayListEntity
import com.example.plmarket.media.data.db.entity.TrackPlayList
import com.example.plmarket.media.data.db.entity.TrackPlayListEntity
import com.example.plmarket.media.domain.module.PlayList
import com.example.plmarket.media.domain.repository.PlayListRepository
import com.example.plmarket.player.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlayListRepositoryImpl(
    private val database: AppDatabase,
    private val playListConvertor: PlayListConvertor,
) : PlayListRepository {

    override fun getPlayList(): Flow<List<PlayList>> = flow {
        val playList = playListConvector(database.playListDao().getPlayLists())
        playList.map {
            it.currentTracks = getTrackForPlayListCount(it)
        }
        emit(playList)
    }

    override suspend fun getPlayList(id: Int): PlayList {
        val playList = database.playListDao().getPlayList(id)
        return playListConvector(playList)
    }

    override suspend fun addPlayList(name: String, description: String, uri: String) {

        val playList = PlayList(name = name, description = description, uri = uri, playListId = 0)

        database.playListDao().insertPlayList(playListConvertorEntity(playList))
    }

    override suspend fun addTrackPlayList(track: Track, playList: PlayList): Boolean {

        if (!database.playListDao().doesTrackExists(trackId = track.trackId)) {
            database.playListDao().addTrack(trackConvectorEntity(track))
        }
        return if (!database.playListDao().doesTrackExistsPlayList(
                trackId = track.trackId,
                playList.playListId
            )
        ) {
            database.playListDao().addTrackForPlayList(
                TrackPlayList(
                    trackId = track.trackId,
                    playListId = playList.playListId
                )
            )
            true
        } else false
    }

    override suspend fun deletePlayList(playListId: Int): Boolean {
        val tracks = trackConvectorTracks(database.playListDao().getTracksByPlayList(playListId))

        database.playListDao().deletePlayListJoinTable(playListId)
        database.playListDao().deletePlayList(playListId)
        for (track in tracks) {
            if (!database.playListDao().doesTrackPlayList(track.trackId)) {
                database.playListDao().deleteTrackPlsyList(track.trackId)
            }
        }
        return true
    }

    override suspend fun deleteTrackPlayList(trackId: String, playListId: Int): Boolean {
        if (!database.playListDao().doesTrackExistsPlayList(trackId, playListId)) {
            database.playListDao().deleteTrackPlsyList(trackId)
        }
        database.playListDao().deleteTrackPlayListJoinTable(trackId, playListId)
        return true
    }

    override suspend fun getTracksForPlayList(playListId: Int): Flow<List<Track>> = flow {
        val tracks = database.playListDao().getTracksByPlayList(playListId)
        emit(trackConvectorTracks(tracks))
    }

    override suspend fun getTrackForPlayListCount(playList: PlayList): Int {
        return database.playListDao().getTracksByPlayList(playList.playListId).size
    }

    override suspend fun updatePlayList(name: String, description: String, uri: String, id: Int) {

        val playList = PlayList(name = name, description = description, uri = uri, playListId = id)

        database.playListDao().updatePlayList(playListConvertorEntity(playList))
    }

    private fun playListConvector(playList: List<PlayListEntity>): List<PlayList> {
        return playList.map { playlist -> playListConvertor.map(playlist) }
    }

    private fun playListConvector(playList: PlayListEntity): PlayList {
        return playListConvertor.map(playList)
    }

    private fun trackConvectorEntity(track: Track): TrackPlayListEntity {
        return playListConvertor.map(track)
    }

    private fun playListConvertorEntity(playList: PlayList): PlayListEntity {

        return playListConvertor.map(playList)
    }

    private fun trackConvectorTracks(tracks: List<TrackPlayListEntity>): List<Track> {
        return tracks.map { tracks -> playListConvertor.map(tracks) }
    }
}