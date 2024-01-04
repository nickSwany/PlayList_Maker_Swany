package com.example.plmarket.search

import com.example.plmarket.search.domain.Impl.TrackHistoryInteractorImpl
import com.example.plmarket.search.domain.Impl.TrackInteractorImpl
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