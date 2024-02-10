package com.example.plmarket.settings.di

import com.example.plmarket.settings.data.SettingsRepositoryImpl
import com.example.plmarket.settings.domain.SettingsRepository
import org.koin.dsl.module

val dataSettingsModule = module {

    single<SettingsRepository> {
        SettingsRepositoryImpl(context = get())
    }
}