package com.example.plmarket.media.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.plmarket.media.data.db.dao.PlayListDao
import com.example.plmarket.media.data.db.dao.TrackDao
import com.example.plmarket.media.data.db.entity.PlayListEntity
import com.example.plmarket.media.data.db.entity.TrackEntity
import com.example.plmarket.media.data.db.entity.TrackPlayList
import com.example.plmarket.media.data.db.entity.TrackPlayListEntity


@Database(
    version = 6,
    entities = [TrackEntity::class,
        PlayListEntity::class,
        TrackPlayListEntity::class,
        TrackPlayList::class],
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun trackDao(): TrackDao
    abstract fun playListDao(): PlayListDao

}