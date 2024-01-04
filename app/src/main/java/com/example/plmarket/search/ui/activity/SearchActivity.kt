package com.example.plmarket.search.ui.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import com.example.pl_market.R
import com.example.pl_market.databinding.ActivitySearchBinding
import com.example.plmarket.player.domain.models.Track
import com.example.plmarket.player.ui.activity.PlayerActivity
import com.example.plmarket.search.data.SearchHistory
import com.example.plmarket.search.ui.adapter.HistoryAdapter
import com.example.plmarket.search.ui.adapter.SearchAdapter
import com.example.plmarket.search.ui.viewModel.SearchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import androidx.recyclerview.widget.LinearLayoutManager


class SearchActivity : AppCompatActivity() {

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        const val EXTRA_TRACK_NAME = "trackName"
        const val EXTRA_ARTIST_NAME = "artistName"
        const val EXTRA_TIME_MILLIS = "timeMillis"
        const val EXTRA_ART_TRACK = "artTrack"
        const val EXTRA_COUNTRY = "country"
        const val EXTRA_YEAR = "year"
        const val EXTRA_GENRE_NAME = "genreName"
        const val EXTRA_COllECTION_NAME = "collectionName"
        const val EXTRA_SONG = "track_song"
    }

    private lateinit var binding: ActivitySearchBinding
    private var searchText: String = ""
    private var flaq = false
    private lateinit var searchHistoryClass: SearchHistory
    private val tracksSearch = ArrayList<Track>()
    private val tracksHistory = ArrayList<Track>()

    private val handler = Handler(Looper.getMainLooper())

    private val viewModel: SearchViewModel by viewModel()

    private var isClickAllowed = true

    private val searchAdapter = SearchAdapter {
        if (clickDebounce()) {
            openPlayer(it)
        }
    }

    private val historyAdapter = HistoryAdapter {
        if (clickDebounce()) {
            openPlayer(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()

        viewModel.addHistoryTracks(tracksHistory)

        viewModel.observeState().observe(this) {
            render(it)
        }

        binding.back.setOnClickListener {
            finish()
        }

        binding.apply {
            cleanHistory.setOnClickListener {
                viewModel.clearTrackListHistory(historyAdapter.trackListHistory)
            }
        }

        viewModel.clearHistory.observe(this) {
            historyAdapter.notifyDataSetChanged()
        }

        binding.apply {
            clean.setOnClickListener {
                tracksSearch.clear()
                binding.searchEdittext.setText("")
                searchAdapter.notifyDataSetChanged()
                //readSearchHistory()  // ломало приложение при нажатие на очистку эдита
                visibleAll()
                visibleHistory()
                hideKeyboard()
            }

            searchEdittext.setOnFocusChangeListener { view, hasFocus ->
                LLSearchHistory.visibility =
                    if (hasFocus && searchEdittext.text.isEmpty() && tracksHistory.isNotEmpty()) View.VISIBLE else View.GONE

            }

            binding.searchEdittext.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    viewModel.searchRequest(searchEdittext.text.toString())
                    true
                } else {
                    false
                }
            }

            binding.searchEdittext.setOnEditorActionListener { _, actionId, _ ->
                actionId == EditorInfo.IME_ACTION_DONE
            }
        }

        binding.RestartSearch.setOnClickListener {
            viewModel.searchRequest(binding.searchEdittext.text.toString())
        }

        binding.searchEdittext.addTextChangedListener {
            binding.clean.isVisible = clearButtonVisibility(it)
            viewModel.searchDebounce(
                changedText = it?.toString() ?: ""
            )
            searchAdapter.notifyDataSetChanged()

            if (searchText.isNotEmpty()) {
                binding.LLSearchHistory.isVisible = false
            }
        }
    }

    private fun render(state: TrackState) {
        when (state) {
            is TrackState.Loading -> showLoading()
            is TrackState.Error -> showError()
            is TrackState.Empty -> showEmpty()
            is TrackState.Content -> showContent(state.tracks)
        }
    }

    private fun showLoading() {
        binding.apply {
            progressBar.isVisible = true
            rcViewHistory.isVisible = false
            messageNotFound.isVisible = false
            placeholderMessageNotInternet.isVisible = false
            placeholderMessage.isVisible = false
            massageNotInternet.isVisible = false
        }
    }

    private fun showError() {
        binding.apply {
            massageNotInternet.isVisible = true
            placeholderMessageNotInternet.isVisible = true
            progressBar.isVisible = false
            placeholderMessageNotInternet.text = getString(R.string.trouble_with_internet)
        }
        hideKeyboard()

    }

    private fun showEmpty() {
        binding.apply {
            messageNotFound.isVisible = true
            placeholderMessage.isVisible = true
            progressBar.isVisible = false
            placeholderMessage.text = getString(R.string.nothing_found)
        }
        hideKeyboard()
    }

    private fun showContent(track: List<Track>) {
        binding.apply {
            RCSearchHistory.isVisible = true
            messageNotFound.isVisible = false
            massageNotInternet.isVisible = false
            progressBar.isVisible = false
        }
        searchAdapter.trackList.clear()
        searchAdapter.trackList.addAll(track)
        searchAdapter.notifyDataSetChanged()
    }

    private fun hideKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun readSearchHistory() {
        tracksHistory.clear()
        tracksHistory.addAll(searchHistoryClass.read())
        historyAdapter.notifyDataSetChanged()
    }

    fun visibleAll() {
        binding.apply {
            rcViewHistory.isVisible = false
            LLSearchHistory.isVisible = true
            messageNotFound.isVisible = false
            placeholderMessage.isVisible = false
            massageNotInternet.isVisible = false
            placeholderMessage.isVisible = false
        }
    }

    fun visibleHistory() {
        if (tracksHistory.isEmpty()) {
            binding.LLSearchHistory.isVisible = false
        } else true
    }

    private fun clearButtonVisibility(s: CharSequence?): Boolean {
        return !s.isNullOrEmpty()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        val textToSave = binding.searchEdittext.text.toString()
        outState.putString("searchText", textToSave)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        val savedText = savedInstanceState.getString("searchText")
        binding.searchEdittext.setText(savedText)
    }

    private fun init() {
        searchAdapter.trackList = tracksSearch
        historyAdapter.trackListHistory = tracksHistory

        binding.apply {
            rcViewHistory.layoutManager =
                LinearLayoutManager(this@SearchActivity, LinearLayoutManager.VERTICAL, false)
            rcViewHistory.adapter = historyAdapter

            RCSearchHistory.layoutManager =
                LinearLayoutManager(this@SearchActivity, LinearLayoutManager.VERTICAL, false)
            RCSearchHistory.adapter = searchAdapter
        }

    }


    override fun onResume() {
        super.onResume()
        if (!flaq) {
            binding.searchEdittext.requestFocus()
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(binding.searchEdittext, InputMethodManager.SHOW_IMPLICIT)
            flaq = true
        }
    }

    override fun onStop() {
        super.onStop()
        viewModel.addHistoryList(historyAdapter.trackListHistory)
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    fun openPlayer(track: Track) {
        viewModel.addHistoryTrack(track)
        openPlayerToIntent(track)
    }

    fun openPlayerToIntent(track: Track) {
        val intent = Intent(this, PlayerActivity::class.java)
        intent.putExtra(EXTRA_TRACK_NAME, track.trackName)
        intent.putExtra(EXTRA_ARTIST_NAME, track.artistName)
        intent.putExtra(EXTRA_TIME_MILLIS, track.trackTimeMillis)
        intent.putExtra(EXTRA_ART_TRACK, track.artworkUrl100)
        intent.putExtra(EXTRA_YEAR, track.releaseDate)
        intent.putExtra(EXTRA_COllECTION_NAME, track.collectionName)
        intent.putExtra(EXTRA_GENRE_NAME, track.primaryGenreName)
        intent.putExtra(EXTRA_COUNTRY, track.country)
        intent.putExtra(EXTRA_SONG, track.previewUrl)
        startActivity(intent)
        searchAdapter.notifyDataSetChanged()
    }
}