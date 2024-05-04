package com.example.plmarket.search.ui.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pl_market.R
import com.example.pl_market.databinding.FragmentSearchBinding
import com.example.plmarket.player.domain.models.Track
import com.example.plmarket.player.ui.activity.PlayerActivity
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

    private lateinit var binding: FragmentSearchBinding
    private var searchText: String = ""
    private var flag = false

    private val tracksSearch = ArrayList<Track>()
    private val tracksHistory = ArrayList<Track>()

    //    private val handler = Handler(Looper.getMainLooper())
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
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()

        viewModel.addHistoryTracks(tracksHistory)

        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }


        binding.apply {
            cleanHistory.setOnClickListener {
                viewModel.clearTrackListHistory(historyAdapter.trackListHistory)
            }
        }
        viewModel.clearHistory.observe(viewLifecycleOwner) {
            binding.cleanHistory.isVisible = false
            binding.youSearch.isVisible = false
        }


        binding.apply {
            clean.setOnClickListener {
                tracksSearch.clear()
                binding.searchEdittext.setText("")
                searchEdittext.clearFocus()
                visibleAll()
                hideKeyboard()
                searchAdapter.notifyDataSetChanged()
                historyAdapter.notifyDataSetChanged()
            }

            searchEdittext.setOnFocusChangeListener { _, hasFocus ->
                LLSearchHistory.visibility =
                    if (hasFocus && searchEdittext.text.isEmpty() &&
                        tracksHistory.isNotEmpty()
                    ) View.VISIBLE else View.GONE

                if (!hasFocus) viewModel.addHistoryList(historyAdapter.trackListHistory)
            }

            searchEdittext.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    viewModel.searchRequest(searchEdittext.text.toString())
                    true
                } else {
                    false
                }
            }
        }

        binding.RestartSearch.setOnClickListener {
            binding.massageNotInternet.isVisible = false
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

    private fun hideKeyboard() {
        val imm =
            requireContext().getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
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
        val textToSave = binding.searchEdittext.text.toString()
        outState.putString(SEARCH_TEXT, textToSave)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        val savedText = savedInstanceState?.getString(SEARCH_TEXT)
        binding.searchEdittext.setText(savedText)
    }

    private fun init() {
        searchAdapter.trackList = tracksSearch
        historyAdapter.trackListHistory = tracksHistory

        binding.apply {
            rcViewHistory.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            rcViewHistory.adapter = searchAdapter

            RCSearchHistory.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            RCSearchHistory.adapter = historyAdapter

            if (tracksHistory.isEmpty()) {
                LLSearchHistory.isVisible = true
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (!flag) {
            binding.searchEdittext.requestFocus()
            val imm =
                requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(binding.searchEdittext, InputMethodManager.SHOW_IMPLICIT)
            flag = true
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
            lifecycleScope.launch {
                delay(CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
        return current
    }

    private fun openPlayer(track: Track) {
        viewModel.addHistoryTrack(track)
        openPlayerToIntent(track)
    }

    private fun openPlayerToIntent(track: Track) {
        /*
        findNavController().navigate(
            R.id.action_searchFragment_to_playerActivity,
            PlayerActivity.createArgs(
                track.trackName,
                track.artistName,
                track.trackTimeMillis,
                track.artworkUrl100,
                track.collectionName,
                track.releaseDate,
                track.country,
                track.primaryGenreName,
                track.previewUrl,
            )
        )
        historyAdapter.notifyDataSetChanged()
      */
        val intent = Intent(requireContext(), PlayerActivity::class.java)
        intent.putExtra(EXTRA_TRACK_NAME, track.trackName)
        intent.putExtra(EXTRA_ARTIST_NAME, track.artistName)
        intent.putExtra(EXTRA_TIME_MILLIS, track.trackTimeMillis)
        intent.putExtra(EXTRA_ART_TRACK, track.artworkUrl100)
        intent.putExtra(EXTRA_YEAR, track.releaseDate)
        intent.putExtra(EXTRA_COllECTION_NAME, track.collectionName)
        intent.putExtra(EXTRA_GENRE_NAME, track.primaryGenreName)
        intent.putExtra(EXTRA_COUNTRY, track.country)
        intent.putExtra(EXTRA_SONG, track.previewUrl)
        intent.putExtra(EXTRA_LIKE, track.isFavorite)
        intent.putExtra(EXTRA_ID, track.trackId)
        intent.putExtra(EXTRA_TRACK, track)
        startActivity(intent)
        historyAdapter.notifyDataSetChanged()
    }
}
