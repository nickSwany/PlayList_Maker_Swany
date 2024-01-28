package com.example.plmarket.search.di.impl

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.example.plmarket.player.domain.models.Track
import com.example.plmarket.search.data.SearchHistory
import com.example.plmarket.search.domain.SharedPreferensecHistory
import com.google.gson.Gson


const val HISTORY_TRACK = "history_track"
const val KEY_HISTORY = "key_history"
const val KEY_HISTORY_ALL = "key_history_all"

class SharedPreferensecHistoryImpl(private val context: Context) : SharedPreferensecHistory {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var searchHistory: SearchHistory

    private fun getSharedPreferences() {
        sharedPreferences = context.getSharedPreferences(
            HISTORY_TRACK, MODE_PRIVATE
        )
    }

    override fun addTrackInAdapter(track: Track) {
        getSharedPreferences()
        val json = Gson().toJson(track)
        sharedPreferences.edit().putString(KEY_HISTORY, json).apply()
    }

    override fun addHistoryTracks(tracksHistory: ArrayList<Track>) {
        getSharedPreferences()

        searchHistory = SearchHistory(sharedPreferences)

        searchHistory.addTrack(tracksHistory)

        sharedPreferences.registerOnSharedPreferenceChangeListener(searchHistory.listener)
    }

    override fun editHistoryList(tracksHistory: ArrayList<Track>) {
        sharedPreferences.edit()
            .putString(KEY_HISTORY_ALL, Gson().toJson(tracksHistory))
            .apply()
    }

    override fun clearTrack(tracksHistory: ArrayList<Track>) {
        sharedPreferences.edit().clear().apply()
        tracksHistory.clear()
    }
}