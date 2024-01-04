package com.example.plmarket

import android.app.Application
import android.content.Context
import com.example.plmarket.settings.data.SettingsRepositoryImpl
import com.example.plmarket.settings.domain.Impl.SettingsInteractorImpl
import com.example.plmarket.settings.domain.SettingsInteractor
import com.example.plmarket.settings.domain.SettingsRepository

object Creator {

    private lateinit var application: Application

    private fun getSettingsRepository(context: Context): SettingsRepository {
        return SettingsRepositoryImpl(context)
    }

    fun provideSettingsInteractor(context: Context): SettingsInteractor {
        return SettingsInteractorImpl(getSettingsRepository(context))
    }
}

