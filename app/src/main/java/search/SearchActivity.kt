package search

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.pl_market.*
import com.example.pl_market.databinding.ActivitySearchBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import search.classes.TrackAdapter
import retrofit2.Response


@Suppress("UNUSED_EXPRESSION")
class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding
    private lateinit var inputEditText: EditText

    companion object {
        var searchQuery = ""
        const val KOD_API = 200
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }

    private val tracks = ArrayList<Track>()
    private val searchHistoryTrack = ArrayList<Track>()
    private var isClickAllowed = true

    private val handler = Handler(Looper.getMainLooper())

    private val searchRunnable = Runnable{startSearchTrack()}

    private lateinit var adapter: TrackAdapter
    private lateinit var searchHistoryAdapter: TrackAdapter
    private lateinit var massageNotInternet: ViewGroup
    private lateinit var placeholderMessage: TextView
    private lateinit var messageNotFound: ViewGroup
    private lateinit var placeholderMessageNotFound: TextView
    private lateinit var LLSearchHistory: ViewGroup
    private lateinit var searchHistoryClass: SearchHistory
    private lateinit var sharedPreferencesHistory: SharedPreferences
    private lateinit var rcViewHistory: RecyclerView
    private lateinit var rcViewSearchHistory: RecyclerView
    private lateinit var progressBar: ProgressBar

    private val iTunesBaseUrl = "https://itunes.apple.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(iTunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val apiService = retrofit.create(ApiService::class.java)

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        inputEditText = findViewById(R.id.search_edittext)
        rcViewHistory = findViewById(R.id.rcViewHistory)
        massageNotInternet = findViewById(R.id.massageNotInternet)
        placeholderMessage = findViewById(R.id.placeholderMessageNotInternet)
        messageNotFound = findViewById(R.id.messageNotFound)
        placeholderMessageNotFound = findViewById(R.id.placeholderMessage)
        LLSearchHistory = findViewById(R.id.LL_searchHistory)
        rcViewSearchHistory = findViewById(R.id.RC_searchHistory)
        progressBar = findViewById(R.id.progressBar)

        sharedPreferencesHistory = getSharedPreferences(HISTORY_KEY, MODE_PRIVATE)

        searchHistoryClass = SearchHistory(sharedPreferencesHistory)

        adapter =
            TrackAdapter(tracks) { tracks ->
                if (clickDebounce()) {
                    openPlayer(tracks)
                    searchHistoryClass.addTrack(tracks)
                    searchHistoryAdapter.notifyDataSetChanged()
                }
            }

        rcViewHistory.adapter = adapter

        searchHistoryAdapter = TrackAdapter(searchHistoryTrack) { searchHistoryTrack ->
            if (clickDebounce()) {
                openPlayer(searchHistoryTrack)
                searchHistoryClass.addTrack(searchHistoryTrack)
                readSearchHistory()
                searchHistoryAdapter.notifyItemRangeChanged(MIN_HISTORY_TRACK, MAX_HISTORY_TRACK)
            }
        }
        rcViewSearchHistory.adapter = searchHistoryAdapter
        readSearchHistory()
        visibleHistory()

        binding.cleanHistory.setOnClickListener {
            searchHistoryClass.clearHistory()
            searchHistoryTrack.clear()
            searchHistoryAdapter.notifyDataSetChanged()
            visibleHistory()
            hideKeyboard()
        }

        binding.back.setOnClickListener {
            finish()
        }

        binding.clean.setOnClickListener {
            tracks.clear()
            inputEditText.setText("")
            adapter.notifyDataSetChanged()
            readSearchHistory()
            visibleAll()
            visibleHistory()
            hideKeyboard()
        }

        binding.RestartSearch.setOnClickListener {
            startSearchTrack()
        }


        inputEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                rcViewHistory.isVisible = true
                binding.clean.visibility = if (s.isNullOrEmpty()) View.GONE else View.VISIBLE
                (s?.toString() ?: "").also { searchQuery = it }
                LLSearchHistory.isVisible = false
                searchDebounce()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        savedInstanceState?.let {
            val savedText = it.getString("searchText")
            inputEditText.setText(savedText)
        }

        inputEditText.setOnFocusChangeListener { view, hasFocus ->
            readSearchHistory()
            rcViewHistory.isVisible = false
            if (searchHistoryTrack.isNotEmpty() && inputEditText.text.isNullOrEmpty()) {
                LLSearchHistory.isVisible = true
            } else false

        }

        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                startSearchTrack()
                true
            } else {
                false
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun startSearchTrack() {
        val searchText = inputEditText.text.toString()
        if (inputEditText.text.isNotEmpty()) {
            progressBar.isVisible = true
            apiService.searchTracks(searchText).enqueue(object : Callback<SearchResponse> {
                override fun onResponse(
                    call: Call<SearchResponse>,
                    response: Response<SearchResponse>

                ) {
                    progressBar.isVisible = false
                    if (response.code() == KOD_API) {
                        tracks.clear()
                        if (response.body()?.results?.isNotEmpty() == true) {
                            tracks.addAll(response.body()?.results!!)
                            adapter.notifyDataSetChanged()
                            massageNotInternet.isVisible = false
                            hideKeyboard()
                        }
                        if (tracks.isEmpty()) {
                            showMessage(getString(R.string.nothing_found), "")
                            hideKeyboard()
                        } else {
                            showMessage("", "")
                            hideKeyboard()
                        }
                    } else {
                        showError(
                            getString(R.string.trouble_with_internet),
                            response.code().toString()
                        )

                        hideKeyboard()
                    }
                }

                override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                    progressBar.visibility = View.GONE
                    showError(
                        getString(R.string.trouble_with_internet),
                        t.message.toString()
                    )
                }
            })
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showError(text: String, additionalMessage: String) {
        if (text.isNotEmpty()) {
            massageNotInternet.isVisible = true
            placeholderMessage.isVisible = true
            tracks.clear()
            adapter.notifyDataSetChanged()
            placeholderMessage.text = text
            placeholderMessageNotFound.isVisible = false
            if (additionalMessage.isNotEmpty()) {
                Toast.makeText(applicationContext, additionalMessage, Toast.LENGTH_LONG)
                    .show()
            }
        } else {
            massageNotInternet.visibility = View.GONE
            hideKeyboard()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showMessage(text: String, additionalMessage: String) {
        if (text.isNotEmpty()) {
            messageNotFound.isVisible = true
            placeholderMessageNotFound.isVisible = true
            tracks.clear()
            adapter.notifyDataSetChanged()
            placeholderMessageNotFound.text = text
            if (additionalMessage.isNotEmpty()) {
                Toast.makeText(applicationContext, additionalMessage, Toast.LENGTH_LONG)
                    .show()
            }
        } else {
            messageNotFound.isVisible = false
            hideKeyboard()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        val textToSave = inputEditText.text.toString()
        outState.putString("searchText", textToSave)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        val savedText = savedInstanceState.getString("searchText")
        inputEditText.setText(savedText)
    }

    private fun hideKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun readSearchHistory() {
        searchHistoryTrack.clear()
        searchHistoryTrack.addAll(searchHistoryClass.read())
        searchHistoryAdapter.notifyDataSetChanged()
    }

    fun visibleAll() {
        rcViewHistory.isVisible = false
        LLSearchHistory.isVisible = true
        messageNotFound.isVisible = false
        placeholderMessageNotFound.isVisible = false
        massageNotInternet.isVisible = false
        placeholderMessage.isVisible = false
    }

    fun visibleHistory() {
        if (searchHistoryTrack.isEmpty()) {
            LLSearchHistory.isVisible = false
        } else true
    }

    fun openPlayer(track: Track) {
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
        adapter.notifyDataSetChanged()
    }

    private fun clickDebounce():Boolean {
        val current = isClickAllowed
        if(isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({isClickAllowed = true}, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }
}
