package com.example.plmarket.settings.ui.activity

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.pl_market.databinding.ActivitySettingsBinding
import com.example.plmarket.App
import com.example.plmarket.DARK_THEME
import com.example.plmarket.THEME_PREFS
import com.example.plmarket.settings.ui.viewModel.SettingsViewModel
import com.example.plmarket.settings.ui.viewModel.SettingsViewModelFactory


class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private lateinit var sharedPreferencesHistory: SharedPreferences
    private lateinit var viewModel: SettingsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel =
            ViewModelProvider(this, SettingsViewModelFactory(this))[SettingsViewModel::class.java]
        binding.themeSwitch.isChecked = (applicationContext as App).switchOn

        binding.themeSwitch.setOnCheckedChangeListener { _, checked ->
            (applicationContext as App).switch_Theme(checked)
            sharedPreferencesHistory = getSharedPreferences(THEME_PREFS, MODE_PRIVATE)
            sharedPreferencesHistory.edit().putBoolean(DARK_THEME, checked).apply()
        }

        binding.back.setOnClickListener {
            finish()
        }
        binding.share.setOnClickListener {
            viewModel.shareCourseLink()
        }
        binding.support.setOnClickListener {
            viewModel.writeSupport()
        }
        binding.userAgreement.setOnClickListener {
            viewModel.openUserAgreement()
        }

    }
}


/*
class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private lateinit var sharedPreferencesHistory: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.themeSwitch.isChecked = (applicationContext as App).switchOn

        binding.themeSwitch.setOnCheckedChangeListener { _, checked  ->
            (applicationContext as App).switch_Theme(checked)
            sharedPreferencesHistory = getSharedPreferences(THEME_PREFS, MODE_PRIVATE)
            sharedPreferencesHistory.edit().putBoolean(DARK_THEME, checked).apply()
        }

        binding.back.setOnClickListener {
            finish()
        }
        binding.share.setOnClickListener {
            shareCourseLink()
        }
        binding.support.setOnClickListener {
            writeSupport()
        }
        binding.userAgreement.setOnClickListener {
            openUserAgreement()
        }
    }

    fun shareCourseLink() {
        val courseLink = getString(R.string.cours_uri)
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, courseLink)
            type = "text/plan"
        }
        startActivity(Intent.createChooser(sendIntent, getString(R.string.share_uri)))
    }

    fun writeSupport() {
        val emailIntent = Intent(Intent.ACTION_SEND)
        emailIntent.type = "text/plan"
        emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.email)))
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.inform))
        emailIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.thanks))
        startActivity((Intent.createChooser(emailIntent, getString(R.string.sent))))
    }

    fun openUserAgreement() {
        val courseLinkUri = Uri.parse(getString(R.string.uri))
        val browserIntent = Intent(Intent.ACTION_VIEW, courseLinkUri)
        startActivity(browserIntent)
    }
}

 */