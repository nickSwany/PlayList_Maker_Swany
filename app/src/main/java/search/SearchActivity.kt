package search

import android.annotation.SuppressLint
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.pl_market.R
import com.example.pl_market.Track
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import search.classes.TrackAdapter
import retrofit2.Response


@Suppress("UNUSED_EXPRESSION")
class SearchActivity : AppCompatActivity() {

    private lateinit var inputEditText: EditText

    companion object {
        var searchQuery = ""
        const val KOD_API = 200
    }

    private val tracks = ArrayList<Track>()
    private val searchHistoryTrack = ArrayList<Track>()

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

        val clearButton = findViewById<ImageView>(R.id.clean)
        val back_BT = findViewById<Button>(R.id.back)
        val restartSearch_BT = findViewById<Button>(R.id.RestartSearch)
        val cleanHistory_BT = findViewById<Button>(R.id.clean_history)

        inputEditText = findViewById(R.id.search_edittext)
        rcViewHistory = findViewById(R.id.rcViewHistory)
        massageNotInternet = findViewById(R.id.massageNotInternet)
        placeholderMessage = findViewById(R.id.placeholderMessageNotInternet)
        messageNotFound = findViewById(R.id.messageNotFound)
        placeholderMessageNotFound = findViewById(R.id.placeholderMessage)
        LLSearchHistory = findViewById(R.id.LL_searchHistory)
        rcViewSearchHistory = findViewById(R.id.RC_searchHistory)

        sharedPreferencesHistory = getSharedPreferences(HISTORY_KEY, MODE_PRIVATE)

        searchHistoryClass = SearchHistory(sharedPreferencesHistory)

        adapter = TrackAdapter(tracks) { tracks ->
            searchHistoryClass.addTrack(tracks)
            searchHistoryAdapter.notifyDataSetChanged()
        }

        rcViewHistory.adapter = adapter

        visibleHistory()

        searchHistoryAdapter = TrackAdapter(searchHistoryTrack) { searchHistoryTrack ->
            searchHistoryClass.addTrack(searchHistoryTrack)
            readSearchHistory()
            searchHistoryAdapter.notifyItemRangeChanged(MIN_HISTORY_TRACK, MAX_HISTORY_TRACK)
        }
        rcViewSearchHistory.adapter = searchHistoryAdapter
        readSearchHistory()


        cleanHistory_BT.setOnClickListener {
            searchHistoryClass.clearHistory()
            searchHistoryTrack.clear()
            searchHistoryAdapter.notifyDataSetChanged()
            visibleHistory()
            hideKeyboard()
        }

        back_BT.setOnClickListener {
            finish()
        }

        clearButton.setOnClickListener {
            tracks.clear()
            inputEditText.setText("")
            adapter.notifyDataSetChanged()
            readSearchHistory()
            visibleAll()
            visibleHistory()
            hideKeyboard()
        }

        restartSearch_BT.setOnClickListener {
            startSearchTrack()
        }

        inputEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                rcViewHistory.isVisible = true
                clearButton.visibility = if (s.isNullOrEmpty()) View.GONE else View.VISIBLE
                (s?.toString() ?: "").also { searchQuery = it }
                LLSearchHistory.isVisible = false
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
            apiService.searchTracks(searchText).enqueue(object : Callback<SearchResponse> {
                override fun onResponse(
                    call: Call<SearchResponse>,
                    response: Response<SearchResponse>

                ) {
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
}
