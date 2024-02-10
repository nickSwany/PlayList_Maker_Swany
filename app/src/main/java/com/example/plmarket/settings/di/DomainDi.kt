package com.example.plmarket.settings.di

import com.example.plmarket.settings.domain.Impl.SettingsInteractorImpl
import com.example.plmarket.settings.domain.SettingsInteractor
import org.koin.dsl.module

val domainSettingsModule = module {

    factory<SettingsInteractor> {
        SettingsInteractorImpl(repository = get())
    }
}