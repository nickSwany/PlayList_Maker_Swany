package com.example.plmarket.media.data

import com.example.plmarket.media.data.db.entity.PlayListEntity
import com.example.plmarket.media.data.db.entity.TrackPlayListEntity
import com.example.plmarket.media.domain.module.PlayList
import com.example.plmarket.player.domain.models.Track

class PlayListConvertor {

    fun map(playList: PlayList): PlayListEntity = PlayListEntity(
        playList.playListId,
        playList.name,
        playList.description.toString(),
        playList.uri,
        playList.currentTracks,
    )


    fun map(playList: PlayListEntity): PlayList = PlayList(
        playList.playListId,
        playList.name,
        playList.description,
        playList.uri,
        playList.currentTracks,
    )

    fun map(track: Track): TrackPlayListEntity = TrackPlayListEntity(
        track.trackId,
        track.trackName,
        track.artistName,
        track.trackTimeMillis,
        track.artworkUrl100,
        track.collectionName,
        track.releaseDate,
        track.primaryGenreName,
        track.country,
        track.previewUrl,
        track.isFavorite,
    )

    fun map(track: TrackPlayListEntity): Track = Track(
        track.trackId,
        track.trackName,
        track.artistName,
        track.trackTimeMillis,
        track.artworkUrl100,
        track.collectionName,
        track.releaseDate,
        track.primaryGenreName,
        track.country,
        track.previewUrl,
        track.isFavorite,
    )

}