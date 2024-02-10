package com.example.plmarket.settings.data

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.pl_market.R
import com.example.plmarket.settings.domain.SettingsRepository

class SettingsRepositoryImpl( val context: Context) : SettingsRepository {
    override fun writeSupport() {
        val emailIntent = Intent(Intent.ACTION_SEND)
        emailIntent.type = "text/plan"
        emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(context.getString(R.string.email)))
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.inform))
        emailIntent.putExtra(Intent.EXTRA_TEXT, context.getString(R.string.thanks))
        context.startActivity((Intent.createChooser(emailIntent, context.getString(R.string.sent))))
    }

    override fun openUserAgreement() {
        val courseLinkUri = Uri.parse(context.getString(R.string.uri))
        val browserIntent = Intent(Intent.ACTION_VIEW, courseLinkUri)
        context.startActivity(browserIntent)
    }

    override fun shareCourseLink() {
        val courseLink = context.getString(R.string.cours_uri)
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, courseLink)
            type = "text/plan"
        }
        context.startActivity(Intent.createChooser(sendIntent, context.getString(R.string.share_uri)))
    }
}