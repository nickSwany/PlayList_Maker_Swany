package com.example.plmarket.search.ui.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pl_market.R
import com.example.pl_market.databinding.FragmentSearchBinding
import com.example.plmarket.player.domain.models.Track
import com.example.plmarket.player.ui.fragment.PlayerFragment
import com.example.plmarket.search.ui.adapter.HistoryAdapter
import com.example.plmarket.search.ui.adapter.SearchAdapter
import com.example.plmarket.search.ui.viewModel.SearchViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {

    companion object {
        const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val SEARCH_TEXT = "SEARCH_TEXT"
        private const val TRACK_TEXT = "TRACK_TEXT"
        const val EXTRA_TRACK_NAME = "trackName"
        const val EXTRA_ARTIST_NAME = "artistName"
        const val EXTRA_TIME_MILLIS = "timeMillis"
        const val EXTRA_ART_TRACK = "artTrack"
        const val EXTRA_COUNTRY = "country"
        const val EXTRA_YEAR = "year"
        const val EXTRA_GENRE_NAME = "genreName"
        const val EXTRA_COllECTION_NAME = "collectionName"
        const val EXTRA_SONG = "track_song"
        const val EXTRA_LIKE = "track_like"
        const val EXTRA_ID = "track_id"
        const val EXTRA_TRACK = "track_track"
    }

    private var _binding: FragmentSearchBinding? = null
    val binding get() = _binding!!
    private var searchText: String = ""
    private var previousSearchText: String? = null

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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()

        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }

        binding.apply {
            cleanHistory.setOnClickListener {
                viewModel.clearTrackListHistory()
                LLSearchHistory.isVisible = false
                historyAdapter.notifyDataSetChanged()
            }
        }

        viewModel.clearHistory.observe(viewLifecycleOwner) {
            binding.LLSearchHistory.isVisible = false
            historyAdapter.notifyDataSetChanged()
        }

        binding.apply {
            clean.setOnClickListener {
                viewModel.clear()
                visibleAll()
                hideKeyboard()
                LLSearchHistory.isVisible = true
                historyAdapter.notifyDataSetChanged()
            }

            searchEdittext.setOnFocusChangeListener { _, hasFocus ->
                LLSearchHistory.isVisible =
                    hasFocus && searchEdittext.text.isEmpty() &&
                            historyAdapter.trackListHistory.isNotEmpty()
            }
        }

        binding.RestartSearch.setOnClickListener {
            binding.massageNotInternet.isVisible = false
            viewModel.searchRequest(binding.searchEdittext.text.toString())
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                //empty
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.clean.isVisible = clearButtonVisibility(s)
                viewModel.searchDebounce(
                    changedText = s?.toString() ?: ""
                )
            }
        }

        binding.searchEdittext.addTextChangedListener(simpleTextWatcher)

        binding.LLSearchHistory.isVisible = false
        binding.youSearch.isVisible = false
        binding.cleanHistory.isVisible = false

        if (savedInstanceState != null) {
            searchText = savedInstanceState.getString(SEARCH_TEXT, "")
            val savedTracks = savedInstanceState.getParcelableArrayList<Track>(TRACK_TEXT)
            if (searchText.isNotEmpty() && savedTracks != null) {
                searchAdapter.trackList.addAll(savedTracks)
                showContent(searchAdapter.trackList)
                binding.searchEdittext.setText(searchText)
                previousSearchText = binding.searchEdittext.text.toString()
            } else {
                showDefault()
            }
        }
    }

    private fun render(state: TrackState) {
        when (state) {
            is TrackState.Loading -> showLoading()
            is TrackState.Error -> showError()
            is TrackState.Empty -> showEmpty()
            is TrackState.Content -> showContent(state.tracks)
            is TrackState.SearchHistory -> showSearchHistory(state.tracks)
            is TrackState.Default -> showDefault()
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
            LLSearchHistory.isVisible = false
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

    @SuppressLint("NotifyDataSetChanged")
    private fun showContent(track: List<Track>) {
        binding.apply {
            RCSearchHistory.isVisible = false
            messageNotFound.isVisible = false
            massageNotInternet.isVisible = false
            progressBar.isVisible = false
            LLSearchHistory.isVisible = false
            placeholderMessageNotInternet.isVisible = false
            cleanHistory.isVisible = false
            youSearch.isVisible = false
            rcViewHistory.isVisible = true
        }
        searchAdapter.trackList.clear()
        searchAdapter.trackList.addAll(track)
        hideKeyboard()
        searchAdapter.notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showSearchHistory(track: List<Track>) {
        binding.apply {
            historyAdapter.trackListHistory.clear()
            historyAdapter.trackListHistory.addAll(track)
            rcViewHistory.isVisible = false
            messageNotFound.isVisible = false
            massageNotInternet.isVisible = false
            if (track.isNotEmpty() && searchEdittext.text.isEmpty() && searchEdittext.isActivated) {
                LLSearchHistory.isVisible = true
                searchAdapter.notifyDataSetChanged()
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showDefault() {
        binding.apply {
            searchEdittext.setText("")
            hideKeyboard()
            searchEdittext.clearFocus()
            progressBar.isVisible = false
            massageNotInternet.isVisible = false
            messageNotFound.isVisible = false
            LLSearchHistory.isVisible = false
            youSearch.isVisible = false
            cleanHistory.isVisible = false
            rcViewHistory.isVisible = false
            searchAdapter.trackList.clear()
            if (historyAdapter.trackListHistory.isNotEmpty()) {
                LLSearchHistory.isVisible = true
                youSearch.isVisible = true
                cleanHistory.isVisible = true
            }
            historyAdapter.notifyDataSetChanged()
        }
    }

    private fun hideKeyboard() {
        val imm =
            context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.searchEdittext.windowToken, 0)
    }

    private fun visibleAll() {
        binding.apply {
            messageNotFound.isVisible = false
            placeholderMessage.isVisible = false
            massageNotInternet.isVisible = false
            placeholderMessageNotInternet.isVisible = false
            LLSearchHistory.isVisible = true
            youSearch.isVisible = true
            RCSearchHistory.isVisible = true
            cleanHistory.isVisible = true
            rcViewHistory.isVisible = false
            FLSearchHistory.isVisible = true
        }
    }

    private fun clearButtonVisibility(s: CharSequence?): Boolean {
        return !s.isNullOrEmpty()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_TEXT, searchText)
        outState.putParcelableArrayList(TRACK_TEXT, ArrayList(searchAdapter.trackList))
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.searchEdittext.setText(savedInstanceState?.getString(SEARCH_TEXT, ""))
        val trackSave = savedInstanceState?.getParcelableArrayList<Track>(TRACK_TEXT)
        if (!trackSave.isNullOrEmpty()) {
            searchAdapter.trackList.addAll(trackSave)
            showContent(trackSave)
        }
    }

    private fun init() {
        binding.apply {
            rcViewHistory.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            rcViewHistory.adapter = searchAdapter

            RCSearchHistory.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            RCSearchHistory.adapter = historyAdapter
        }
    }

    override fun onResume() {
        super.onResume()
        val currentSearchText = binding.searchEdittext.text.toString()

        if (currentSearchText.isBlank()) {
            showDefault()
        } else if (currentSearchText != searchText) {
            viewModel.searchRequest(currentSearchText)
        } else {
            showContent(searchAdapter.trackList)
        }
    }

    override fun onStop() {
        super.onStop()
        if (binding.searchEdittext.text.isEmpty()) viewModel.clear()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            lifecycleScope.launch {
                delay(CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
        return current
    }

    private fun openPlayer(track: Track) {
        viewModel.saveTrack(track)
        openPlayerToIntent(track)
    }

    private fun openPlayerToIntent(track: Track) {
        findNavController().navigate(
            R.id.action_searchFragment_to_playerFragment2,
            PlayerFragment.createArgs(
                track.trackId,
                track.trackName.toString(),
                track.artistName,
                track.trackTimeMillis,
                track.artworkUrl100,
                track.collectionName,
                track.releaseDate,
                track.country,
                track.primaryGenreName,
                track.previewUrl,
                track.isFavorite,
                track
            )
        )
    }
}
