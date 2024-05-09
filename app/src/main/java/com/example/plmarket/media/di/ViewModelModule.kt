package com.example.plmarket.media.di

import com.example.plmarket.media.ui.view_model.FavoriteTracksViewModel
import com.example.plmarket.media.ui.view_model.NewPlayListViewModel
import com.example.plmarket.media.ui.view_model.PlayListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewMediaViewModel = module {

    viewModel {
        FavoriteTracksViewModel(get())
    }

    viewModel {
        NewPlayListViewModel(get())
    }

    viewModel {
        PlayListViewModel(get())
    }
}