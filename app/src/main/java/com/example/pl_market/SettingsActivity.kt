package com.example.pl_market

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val backButton = findViewById<Button>(R.id.back)
        val shareView = findViewById<ImageView>(R.id.share)
        val supportView = findViewById<ImageView>(R.id.support)
        val userAgreement = findViewById<ImageView>(R.id.userAgreement)

        backButton.setOnClickListener {
            finish()
        }
        shareView.setOnClickListener {
            shareCourseLink()
        }
        supportView.setOnClickListener {
            writeSupport()
        }
        userAgreement.setOnClickListener {
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