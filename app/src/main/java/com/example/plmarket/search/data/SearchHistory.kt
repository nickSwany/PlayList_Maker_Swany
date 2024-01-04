package com.example.plmarket.search.data

import android.content.SharedPreferences
import android.util.Log
import com.example.plmarket.player.domain.models.Track
import com.example.plmarket.search.domain.Impl.KEY_HISTORY
import com.example.plmarket.search.ui.adapter.HistoryAdapter
import com.google.gson.Gson

class SearchHistory(private val sharedPrefs: SharedPreferences) {

    lateinit var listener: SharedPreferences.OnSharedPreferenceChangeListener

    companion object {
         const val HISTORY_KEY = "history_key"
         const val MAX_HISTORY_TRACK = 10
    }

    private val historyAdapter = HistoryAdapter {}

    fun read(): Array<Track> {
        val json = sharedPrefs.getString(HISTORY_KEY,null) ?: return emptyArray()
        return Gson().fromJson(json, Array<Track>::class.java)
    }

    fun addTrack(tracksHistory: ArrayList<Track>) {
        historyAdapter.trackListHistory = tracksHistory
        tracksHistory.addAll(read())
        listener = SharedPreferences.OnSharedPreferenceChangeListener { sharedPrefHistory, key ->
            if (key == KEY_HISTORY) {
                val track = sharedPrefHistory?.getString(key, null)
                if (track != null) {
                    repeatingTrackCheck(track, tracksHistory)
                    if (historyAdapter.trackListHistory.size >= MAX_HISTORY_TRACK) {
                        tracksHistory.removeAt(historyAdapter.trackListHistory.size - 1)
                    }
                    historyAdapter.trackListHistory.add(0, createTrackFromJson(track))
                    historyAdapter.notifyItemInserted(0)

                }
            }
        }
    }

    private fun createTrackFromJson(json: String?): Track {
        return Gson().fromJson(json, Track::class.java)
    }
    private fun repeatingTrackCheck(track: String, tracksHistory: ArrayList<Track>) {
        val iterator = tracksHistory.iterator()
        while (iterator.hasNext()) {
            val item = iterator.next()
            if (item.trackId == createTrackFromJson(track).trackId) {
                iterator.remove()
            }
        }
    }
   // fun clearHistory() = sharedPrefs.edit().clear().apply()
}