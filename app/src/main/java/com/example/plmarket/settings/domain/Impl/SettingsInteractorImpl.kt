package com.example.plmarket.settings.domain.Impl

import com.example.plmarket.settings.domain.SettingsInteractor
import com.example.plmarket.settings.domain.SettingsRepository

class SettingsInteractorImpl(private val repository: SettingsRepository) : SettingsInteractor {
    override fun openUserAgreement() {
        repository.openUserAgreement()
    }

    override fun shareCourseLink() {
        repository.shareCourseLink()
    }

    override fun writeSupport() {
        repository.writeSupport()
    }
}