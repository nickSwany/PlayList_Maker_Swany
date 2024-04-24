package com.example.plmarket.media.di

import com.example.plmarket.media.domain.db.FavoriteInteractor
import com.example.plmarket.media.domain.impl.FavoriteInteractorImpl
import org.koin.dsl.module

val interactorFavoriteModule = module {
    single<FavoriteInteractor> {
        FavoriteInteractorImpl(get())
    }
}