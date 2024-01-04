package com.example.plmarket.search


import com.example.plmarket.search.ui.viewModel.SearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewSearchViewModel = module {
    viewModel{
        SearchViewModel(application = get(),
            interactorHistory = get(), interactorSearch = get())
    }
}