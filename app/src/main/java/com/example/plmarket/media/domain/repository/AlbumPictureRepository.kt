package com.example.plmarket.media.domain.repository

import android.net.Uri

interface AlbumPictureRepository {

    fun saveImageToPrivateStorage(uri:Uri)

    fun getUri(uriPlayList: String): String
}