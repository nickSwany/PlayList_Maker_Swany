package com.example.plmarket.media.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import androidx.core.net.toUri
import com.example.plmarket.media.domain.repository.AlbumPictureRepository
import java.io.File
import java.io.FileOutputStream


class AlbumPictureRepositoryImpl(val context: Context) : AlbumPictureRepository {

    val path = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)

    private val nameAlbum = "myalbum"

    override fun saveImageToPrivateStorage(uri: Uri) {

        val uriPlayList =
            "${uri.toString().substringBefore("/")}.jpg" //.reversed() после toString()

        val filePath = File(path, nameAlbum)

        if (!filePath.exists()) {
            filePath.mkdirs()
        }

        val file = File(filePath, uriPlayList)

        val inputStream = context.contentResolver.openInputStream(uri)

        val outputStream = FileOutputStream(file)

        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
    }

    override fun getUri(uriPlayList: String): String {

        val filePath = File(path.toString(), nameAlbum)

        val file = File(filePath, uriPlayList)

        return file.toUri().toString()
    }
}