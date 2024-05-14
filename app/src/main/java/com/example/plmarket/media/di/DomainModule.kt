package com.example.plmarket.media.di

import com.example.plmarket.media.domain.db.FavoriteInteractor
import com.example.plmarket.media.domain.db.PlayListInteractor
import com.example.plmarket.media.domain.impl.FavoriteInteractorImpl
import com.example.plmarket.media.domain.impl.PlayListInteractorImpl
import org.koin.dsl.module

val interactorFavoriteModule = module {
    single<FavoriteInteractor> {
        FavoriteInteractorImpl(get())
    }
    single<PlayListInteractor>{
        PlayListInteractorImpl(get(), get())
    }
}