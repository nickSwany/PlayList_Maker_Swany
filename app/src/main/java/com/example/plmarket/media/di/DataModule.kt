package com.example.plmarket.media.di

import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.plmarket.media.data.FavoriteRepositoryImpl
import com.example.plmarket.media.data.TrackDbConvertor
import com.example.plmarket.media.data.db.AppDatabase
import com.example.plmarket.media.data.repository.FavoriteRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryMediaModule = module {
    factory {
        TrackDbConvertor()
    }

    single<FavoriteRepository> {
        FavoriteRepositoryImpl(get(), get())
    }
}

val dataModule = module {

    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database.db")
            .fallbackToDestructiveMigration()
            .build()
    }
}