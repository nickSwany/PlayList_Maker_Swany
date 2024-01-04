package com.example.plmarket.settings.ui.viewModel

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.example.plmarket.settings.domain.SettingsInteractor

class SettingsViewModel(
    private val settingsInteractor: SettingsInteractor
) : ViewModel() {

    fun shareCourseLink() {
        settingsInteractor.shareCourseLink()
    }

    fun writeSupport() {
        settingsInteractor.writeSupport()
    }

    fun openUserAgreement() {
        settingsInteractor.openUserAgreement()
    }

}