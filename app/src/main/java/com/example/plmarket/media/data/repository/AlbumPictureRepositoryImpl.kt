package com.example.plmarket.media.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Environment
import androidx.core.net.toUri
import androidx.exifinterface.media.ExifInterface
import com.example.plmarket.media.domain.repository.AlbumPictureRepository
import java.io.File
import java.io.FileOutputStream

class AlbumPictureRepositoryImpl(val context: Context) : AlbumPictureRepository {

    val path = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)

    private val nameAlbum = "myalbum"

    override fun saveImageToPrivateStorage(uri: Uri) {

        val filePath = File(path, nameAlbum)

        if (!filePath.exists()) {
            filePath.mkdirs()
        }

        val uriPlayList =
            "${uri.toString().reversed().substringBefore("/")}.jpg"

        val file = File(filePath, uriPlayList)
        val exif = ExifInterface(context.contentResolver.openInputStream(uri)!!)
        val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
        context.contentResolver.openInputStream(uri).use { inputStream ->
            FileOutputStream(file).use { outputStream ->
                val matrix = Matrix()
                when (orientation) {
                    ExifInterface.ORIENTATION_ROTATE_90 -> matrix.postRotate(90f)
                    ExifInterface.ORIENTATION_ROTATE_180 -> matrix.postRotate(180f)
                    ExifInterface.ORIENTATION_ROTATE_270 -> matrix.postRotate(270f)
                    else -> Unit
                }
                val bitmap = BitmapFactory.decodeStream(inputStream)
                val rotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
                rotated.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)
            }
        }
    }

    override fun getUri(uriPlayList: String): String {

        val filePath = File(path.toString(), nameAlbum)

        val file = File(filePath, uriPlayList)

        return file.toUri().toString()
    }
  }