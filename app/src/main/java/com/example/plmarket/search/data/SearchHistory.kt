package com.example.plmarket.search.data

import android.content.SharedPreferences
import com.example.plmarket.player.domain.models.Track
import com.example.plmarket.search.di.impl.KEY_HISTORY
import com.example.plmarket.search.di.impl.KEY_HISTORY_ALL
import com.example.plmarket.search.ui.adapter.HistoryAdapter
import com.google.gson.Gson

class SearchHistory(private val sharedPrefs: SharedPreferences) {

    lateinit var listener: SharedPreferences.OnSharedPreferenceChangeListener

    companion object {
        const val MAX_HISTORY_TRACK = 10
    }

    private val historyAdapter = HistoryAdapter {}

    private fun read(sharedPreferences: SharedPreferences): Array<Track> {
        val json = sharedPreferences.getString(KEY_HISTORY_ALL, null) ?: return emptyArray()
        return Gson().fromJson(json, Array<Track>::class.java)
    }

    fun addTrack(tracksHistory: ArrayList<Track>) {
        historyAdapter.trackListHistory = tracksHistory
        tracksHistory.addAll(read(sharedPrefs))
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
}