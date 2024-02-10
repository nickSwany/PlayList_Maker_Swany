package com.example.plmarket

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.plmarket.player.di.dataPlayerModule
import com.example.plmarket.player.di.domainPlayerModule
import com.example.plmarket.player.di.viewPlayerModelModule
import com.example.plmarket.search.di.dataSearchModule
import com.example.plmarket.search.di.domainSearchModule
import com.example.plmarket.search.di.viewSearchViewModel
import com.example.plmarket.settings.di.dataSettingsModule
import com.example.plmarket.settings.di.domainSettingsModule
import com.example.plmarket.settings.di.viewSettingsViewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

const val THEME_PREFS = "theme_prefs"
const val DARK_THEME = "dark_theme"

class App : Application() {

    var switchOn: Boolean = false

    override fun onCreate() {
        super.onCreate()
        val sharedPreferences = getSharedPreferences(THEME_PREFS, MODE_PRIVATE)
        switchOn = sharedPreferences.getBoolean(DARK_THEME, false)

        switch_Theme(switchOn)

        startKoin {
            androidContext(this@App)

            modules(
                listOf(
                    viewSearchViewModel,
                    viewPlayerModelModule,
                    dataPlayerModule, domainPlayerModule,
                    domainSearchModule, dataSearchModule,
                    dataSettingsModule, domainSettingsModule, viewSettingsViewModelModule
                )
            )
        }
    }

    fun switch_Theme(switchTheme: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            when (switchTheme) {
                true -> AppCompatDelegate.MODE_NIGHT_YES
                false -> AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}