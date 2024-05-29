package com.example.plmarket.search.di.impl

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.example.plmarket.player.domain.models.Track
import com.example.plmarket.search.data.SearchHistory
import com.example.plmarket.search.data.SearchHistory.Companion.MAX_HISTORY_TRACK
import com.example.plmarket.search.domain.SharedPreferensecHistory
import com.google.gson.Gson


const val HISTORY_TRACK = "history_track"
const val KEY_HISTORY = "key_history"
const val KEY_HISTORY_ALL = "key_history_all"

class SharedPreferensecHistoryImpl(private val context: Context) : SharedPreferensecHistory {
    private lateinit var sharedPreferences: SharedPreferences
    private var trackHistory = mutableListOf<Track>()

    private fun getSharedPreferences() {
        sharedPreferences = context.getSharedPreferences(
            HISTORY_TRACK, MODE_PRIVATE
        )
    }

    override fun saveTrack(track: Track) {
        addHistoryTrack(track)
    }

    private fun addHistoryTrack(newTrack: Track) {
        trackHistory = read()
        if (trackHistory.contains(newTrack)) trackHistory.remove(newTrack)
        if (trackHistory.size == MAX_HISTORY_TRACK) {
            trackHistory.removeLast()
        }
        trackHistory.add(0, newTrack)
        writeHistoryToJson(trackHistory)
    }

    private fun writeHistoryToJson(searchTrackHistory: List<Track>) {
        val json = Gson().toJson(searchTrackHistory)
        sharedPreferences.edit().putString(KEY_HISTORY_ALL, json).apply()
    }

    override fun getAllTrack(): List<Track> {
        getSharedPreferences()
        trackHistory = read()
        return trackHistory
    }

    override fun clearTrack() {
        sharedPreferences.edit()
            .clear()
            .apply()
        trackHistory.clear()
    }

    override fun editHistoryList() {
        sharedPreferences.edit()
            .putString(KEY_HISTORY_ALL, Gson().toJson(trackHistory))
    }

    private fun read(): MutableList<Track> {
        val json = sharedPreferences.getString(KEY_HISTORY_ALL, null) ?: return mutableListOf()
        return Gson().fromJson(json, Array<Track>::class.java).toMutableList()
    }
}