package com.example.plmarket.media.data

import com.example.plmarket.media.data.db.entity.TrackEntity
import com.example.plmarket.player.domain.models.Track

class TrackDbConvertor {

    fun map(track: Track): TrackEntity = TrackEntity(
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


    fun map(track: TrackEntity): Track = Track(
        track.trackId,
        track.trackName,
        track.artistName.toString(),
        track.trackTimeMillis.toString(),
        track.artworkUrl100,
        track.collectionName,
        track.releaseDate,
        track.primaryGenreName,
        track.country,
        track.previewUrl,
    )

}