package com.example.plmarket.media.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.plmarket.media.data.db.dao.TrackDao
import com.example.plmarket.media.data.db.entity.TrackEntity


@Database(version = 3,
    entities = [TrackEntity::class],
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun trackDao(): TrackDao

}