package com.example.plmarket.media.data.db.dao

import androidx.room.*
import com.example.plmarket.media.data.db.entity.TrackEntity

@Dao
interface TrackDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTracks(tracks: TrackEntity)

    @Query("SELECT * FROM track_table")
    suspend fun getTracks(): List<TrackEntity>

    @Query("SELECT trackId FROM track_table")
    suspend fun getTracksId(): List<String>

    @Query("DELETE FROM track_table WHERE trackId is :id")
    suspend fun deleteTrackEntity(id: String)

    @Query("SELECT EXISTS(SELECT 1 FROM track_table WHERE trackId = :trackId)")
    suspend fun doesTrackExist(trackId: String) : Boolean
}