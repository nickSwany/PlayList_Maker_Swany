package com.example.plmarket.media.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.plmarket.media.data.db.entity.PlayListEntity
import com.example.plmarket.media.data.db.entity.TrackPlayList
import com.example.plmarket.media.data.db.entity.TrackPlayListEntity

@Dao
interface PlayListDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlayList(playListEntity: PlayListEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTrackForPlayList(trackPlayList: TrackPlayList)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTrack(trackPlayList: TrackPlayListEntity)

    @Update
    suspend fun updatePlayList(playListEntity: PlayListEntity)

    @Query("SELECT * FROM playlist")
    suspend fun getPlayLists(): List<PlayListEntity>

    @Query("SELECT * FROM playlist WHERE playListId =:id")
    suspend fun getPlayList(id: Int): PlayListEntity

    @Query("SELECT EXISTS(SELECT 1 FROM playlist_for_tracks WHERE trackId =:trackId)")
    suspend fun doesTrackExists(trackId: String): Boolean

    @Query("SELECT count(playListId) > 0 FROM tracks_playlist WHERE trackId =:trackId AND playListId =:playlistId")
    suspend fun doesTrackExistsPlayList(trackId: String, playlistId: Int): Boolean

    @Query("SELECT * FROM playlist_for_tracks INNER JOIN tracks_playlist ON playlist_for_tracks.trackId == tracks_playlist.trackId WHERE tracks_playlist.playListId == :playListId")
    suspend fun getTracksByPlayList(playListId: Int): List<TrackPlayListEntity>

    @Query("DELETE FROM playlist_for_tracks WHERE trackId is :id")
    suspend fun deleteTrackPlayList(id: String)

    @Query("DELETE FROM tracks_playlist WHERE trackId = :id AND playListId = :idPlaylist")
    suspend fun deleteTrackPlayListJoinTable(id: String, idPlaylist: Int)

    @Query("DELETE FROM playlist WHERE playListId = :idPlaylist")
    suspend fun deletePlayList(idPlaylist: Int)

    @Query("DELETE FROM tracks_playlist WHERE playlistId = :idPlaylist")
    suspend fun deletePlayListJoinTable(idPlaylist: Int)

    @Query("SELECT count(playlistId) > 0 FROM tracks_playlist WHERE trackId =:trackId")
    suspend fun doesTrackPlayList(trackId: String): Boolean
}