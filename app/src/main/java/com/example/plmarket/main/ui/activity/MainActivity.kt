package com.example.plmarket.main.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.pl_market.R
import com.example.pl_market.databinding.ActivityMainBinding
import com.example.plmarket.media.MediatecaActivity
import com.example.plmarket.search.ui.activity.SearchActivity
import com.example.plmarket.settings.ui.activity.SettingsActivity


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_main)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.search.setOnClickListener {
            val searchIntent = Intent(this, SearchActivity::class.java)
            startActivity(searchIntent)
        }

        binding.mediateka.setOnClickListener {
            val mediatecaIntent = Intent(this, MediatecaActivity::class.java)
            startActivity(mediatecaIntent)
        }

        binding.settings.setOnClickListener {
            val settingsIntent = Intent(this, SettingsActivity::class.java)
            startActivity(settingsIntent)
        }
    }
}


