package com.example.plmarket.player.di

import com.example.plmarket.player.data.PlayerRepositoryImpl
import com.example.plmarket.player.domain.api.PlayerRepository
import org.koin.dsl.module

val dataPlayerModule = module {

    factory<PlayerRepository> {
        PlayerRepositoryImpl()
    }
}