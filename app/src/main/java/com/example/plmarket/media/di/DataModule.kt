package com.example.plmarket.media.di

import androidx.room.Room
import com.example.plmarket.media.data.PlayListConvertor
import com.example.plmarket.media.data.repository.FavoriteRepositoryImpl
import com.example.plmarket.media.data.TrackDbConvertor
import com.example.plmarket.media.data.db.AppDatabase
import com.example.plmarket.media.data.repository.AlbumPictureRepositoryImpl
import com.example.plmarket.media.data.repository.PlayListRepositoryImpl
import com.example.plmarket.media.domain.repository.AlbumPictureRepository
import com.example.plmarket.media.domain.repository.FavoriteRepository
import com.example.plmarket.media.domain.repository.PlayListRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryMediaModule = module {
    factory {
        TrackDbConvertor()
    }

    factory {
        PlayListConvertor()
    }

    single<FavoriteRepository> {
        FavoriteRepositoryImpl(get(), get())
    }

    single<AlbumPictureRepository>{
        AlbumPictureRepositoryImpl(get())
    }

    single<PlayListRepository>{
        PlayListRepositoryImpl(get(), get(), get())
    }
}

val dataModule = module {

    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database.db")
            .fallbackToDestructiveMigration()
            .build()
    }
}