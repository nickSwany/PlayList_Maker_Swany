package com.example.plmarket.media.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist")
data class PlayListEntity(
    @PrimaryKey(autoGenerate = true)
    val playListId: Int,
    val name: String,
    val description: String,
    val uri: String?,
    val currentTracks: Int = 0
)
