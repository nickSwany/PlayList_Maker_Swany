package com.example.plmarket.search.di

import com.example.plmarket.search.di.impl.TrackHistoryInteractorImpl
import com.example.plmarket.search.di.impl.TrackInteractorImpl
import com.example.plmarket.search.domain.TrackHistoryInteractor
import com.example.plmarket.search.domain.TrackInteractor
import org.koin.dsl.module

val domainSearchModule = module {

    factory<TrackInteractor> {
        TrackInteractorImpl(repository = get())
    }

    factory<TrackHistoryInteractor> {
        TrackHistoryInteractorImpl(sharedHistory = get())
    }
}