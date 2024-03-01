package com.example.plmarket.search.di

import com.example.plmarket.search.ui.viewModel.SearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewSearchViewModel = module {

    viewModel {
        SearchViewModel(interactorSearch = get(), interactorHistory = get())
    }
}