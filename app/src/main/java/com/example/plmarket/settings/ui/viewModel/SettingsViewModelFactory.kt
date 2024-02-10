package com.example.plmarket.settings.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.plmarket.Creator.provideSettingsInteractor
import android.content.Context

class SettingsViewModelFactory(var context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SettingsViewModel(
            provideSettingsInteractor(context = context)
        ) as T

    }
}


