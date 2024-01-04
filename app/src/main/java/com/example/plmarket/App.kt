package com.example.plmarket

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.plmarket.search.dataSearchModule
import com.example.plmarket.search.domainSearchModule
import com.example.plmarket.search.viewSearchViewModel
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
                    dataSearchModule, domainSearchModule, viewSearchViewModel
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