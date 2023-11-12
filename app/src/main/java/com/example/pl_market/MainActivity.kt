package com.example.pl_market

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.os.PersistableBundle
import android.widget.Button
import search.SearchActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_main)
        super.onCreate(savedInstanceState)


        val searchButton = findViewById<Button>(R.id.search)
        val mediatecaButton = findViewById<Button>(R.id.mediateka)
        val settingsButton = findViewById<Button>(R.id.settings)

        searchButton.setOnClickListener {
            val searchIntent = Intent(this, SearchActivity::class.java)
            startActivity(searchIntent)
        }

        mediatecaButton.setOnClickListener {
            val mediatecaIntent = Intent(this, MediatecaActivity::class.java)
            startActivity(mediatecaIntent)
        }

        settingsButton.setOnClickListener {
            val settingsIntent = Intent(this, SettingsActivity::class.java)
            startActivity(settingsIntent)
        }
    }
}