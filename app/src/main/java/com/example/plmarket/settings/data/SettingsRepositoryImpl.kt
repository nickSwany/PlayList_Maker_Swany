package com.example.plmarket.settings.data

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat.startActivity
import com.example.pl_market.R
import com.example.plmarket.settings.domain.SettingsRepository

class SettingsRepositoryImpl(private val context: Context) : SettingsRepository {
    override fun writeSupport() {
        val emailIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plan"
            putExtra(Intent.EXTRA_EMAIL, arrayOf(context.getString(R.string.email)))
            putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.inform))
            putExtra(Intent.EXTRA_TEXT, context.getString(R.string.thanks))
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(emailIntent)
    }

    override fun openUserAgreement() {
        val courseLinkUri = Uri.parse(context.getString(R.string.uri))
        val browserIntent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(courseLinkUri.toString())
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(browserIntent)
    }

    override fun shareCourseLink() {
        val courseLink = context.getString(R.string.cours_uri)
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, courseLink)
            type = "text/plan"
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(sendIntent)
    }
}