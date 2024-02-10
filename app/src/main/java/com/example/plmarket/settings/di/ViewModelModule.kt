package com.example.plmarket.settings.di

import com.example.plmarket.settings.ui.viewModel.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewSettingsViewModelModule = module {

    viewModel {
        SettingsViewModel(settingsInteractor = get())
    }
}