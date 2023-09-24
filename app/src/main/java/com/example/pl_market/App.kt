package com.example.pl_market

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

const val THEME_PREFS = "theme_prefs"
const val DARK_THEME = "dark_theme"

class App : Application() {

    var switchOn: Boolean = false

    override fun onCreate() {
        super.onCreate()
        val sharedPreferences = getSharedPreferences(THEME_PREFS, MODE_PRIVATE)
        switchOn = sharedPreferences.getBoolean(DARK_THEME, false)

        switch_Theme(switchOn)
    }

    fun switch_Theme(switchTheme: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            when (switchTheme){
                true -> AppCompatDelegate.MODE_NIGHT_YES
                false -> AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}