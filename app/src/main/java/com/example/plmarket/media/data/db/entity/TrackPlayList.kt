package com.example.plmarket.media.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "tracks_playlist"
    /*
    ,primaryKeys = ["trackId", "playListId"]
    foreignKeys =[
    ForeignKeys(
    entity = TrackplayListEntity::class,
    parentColumns =["trackId"],
    childColumns = ["trackId"],
    onDelete = ForeignKey.CASCADE,
    onUpdate = ForeignKey.CASCADE
    ),
    ForeignKey(entity = PlayListEntity::class,
    parentColumns = ["playListId"],
    childColumns = ["playListId"],
    onDelete = ForeignKey.CASCADE,
    onUpdete = ForeignKey.CASCADE)
    ]
     */
)
data class TrackPlayList(
    @PrimaryKey
    val playListId: Int,
    val trackId: String,
)
