package com.example.plmarket.media.data.repository

import android.content.Context
import android.content.Intent
import android.util.Log
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
import java.text.SimpleDateFormat
import java.util.*

class PlayListRepositoryImpl(
    private val database: AppDatabase,
    private val playListConvertor: PlayListConvertor,
    private val context: Context
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
            Log.d("addTrackPlaylist", "true ")
            true
        } else {
            Log.d("addTrackPlaylist", "false")
            false
        }
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

    override suspend fun getTracksForPlayList(playList: Int): Flow<List<Track>> = flow {
        val tracks = database.playListDao().getTracksByPlayList(playList)
        emit(trackConvectorTracks(tracks))
    }

    override suspend fun getTrackForPlayListCount(playList: PlayList): Int {
        return database.playListDao().getTracksByPlayList(playList.playListId).size
    }

    override suspend fun deletePlayList(playListId: Int): Boolean {
        val tracks = trackConvectorTracks(database.playListDao().getTracksByPlayList(playListId))
        database.playListDao().deletePlayList(playListId)
        database.playListDao().deletePlayListJoinTable(playListId)
        for (track in tracks) {
            if (!database.playListDao().doesTrackPlayList(track.trackId)) {
                database.playListDao().deleteTrackPlayList(track.trackId)
            }
        }
        return true
    }

    override suspend fun deleteTrackPlayList(trackId: String, playListId: Int): Boolean {
        if (!database.playListDao().doesTrackExistsPlayList(trackId, playListId)) {
            database.playListDao().deleteTrackPlayList(trackId)
        }
        database.playListDao().deleteTrackPlayListJoinTable(trackId, playListId)
        return true
    }

    override fun sharePlayList(
        tracks: List<Track>,
        nameTrack: String,
        description: String,
        currentTrack: String
    ) {
        var listTrack = ""
        var count = 1
        for (track in tracks) {
            val timeTrack =
                SimpleDateFormat(
                    "mm:ss",
                    Locale.getDefault()
                ).format(track.trackTimeMillis?.toInt())
            listTrack += "$count. ${track.artistName} - ${track.trackName} (${timeTrack})"
            count++
        }
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plsin"
            putExtra(
                Intent.EXTRA_TEXT, """
                $nameTrack
                $description
                $currentTrack
            """.trimIndent() + listTrack
            ) // или листр трек нужно вынести
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            Intent.createChooser(this, null)
        }
        context.startActivity(intent, null)
    }

    private fun trackConvectorTracks(tracks: List<TrackPlayListEntity>): List<Track> {
        return tracks.map { tracks -> playListConvertor.map(tracks) }
    }


}