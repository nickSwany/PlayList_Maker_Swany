package com.example.plmarket.search

import android.content.SharedPreferences
import android.util.Log
import com.example.plmarket.player.domain.models.Track
import com.google.gson.Gson

const val HISTORY_KEY = "history_key"
const val MAX_HISTORY_TRACK = 10
const val MIN_HISTORY_TRACK = 0

class SearchHistory(sharedPreferences: SharedPreferences) {

    private val sharedPrefs = sharedPreferences

    fun read(): List<Track> {
        val json = sharedPrefs.getString(HISTORY_KEY, null) ?: return emptyList()
        return Gson().fromJson(json, Array<Track>::class.java).toList()
    }

    fun addTrack(trackHistory: Track) {
        var trackList = read().toMutableList()
        trackList.removeIf { it.trackId == trackHistory.trackId}
        trackList.add(0, trackHistory)
        if (trackList.size > MAX_HISTORY_TRACK)
            trackList = ArrayList(trackList.dropLast(1))
        val json = Gson().toJson(trackList)
        sharedPrefs.edit()
            .putString(HISTORY_KEY, json)
            .apply()

        Log.d("SearchHistory", "Трек добавлен: ${trackHistory.trackId}")
    }

    fun clearHistory() = sharedPrefs.edit().clear().apply()
}