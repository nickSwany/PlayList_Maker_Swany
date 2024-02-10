package com.example.plmarket.player.di

import com.example.plmarket.player.domain.api.PlayerInteractor
import com.example.plmarket.player.domain.impl.PlayerInteractorImpl
import org.koin.dsl.module

val domainPlayerModule = module {
    factory<PlayerInteractor> {
        PlayerInteractorImpl(repository = get())
    }
}