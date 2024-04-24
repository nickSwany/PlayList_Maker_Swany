package com.example.plmarket.player.di

import com.example.plmarket.player.ui.viewModel.PlayerViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewPlayerModelModule = module {

    viewModel {
        PlayerViewModel(get(), get())
    }
}