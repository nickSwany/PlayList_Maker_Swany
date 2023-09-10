package search

import android.graphics.Color
import android.media.Ringtone
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.text.Editable
import android.text.Spannable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pl_market.R
import com.example.pl_market.Track
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import search.classes.TrackAdapter
import java.util.concurrent.TimeUnit
import retrofit2.Response


class SearchActivity : AppCompatActivity() {

    private lateinit var inputEditText: EditText

    companion object {
        var searchQuery = ""
        const val SEARCH_STRING = "SEARCH_STRING"
    }

    private val tracks = ArrayList<Track>()

    val adapter = TrackAdapter()

    private lateinit var massageNotInternet: ViewGroup
    private lateinit var placeholderMessage: TextView
    private lateinit var messageNotFound: ViewGroup
    private lateinit var placeholderMessageNotFound: TextView

    private val iTunesBaseUrl = "https://itunes.apple.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(iTunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val apiService = retrofit.create(ApiService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val rcViewHistory = findViewById<RecyclerView>(R.id.rcViewHistory)
        rcViewHistory.adapter = adapter
        rcViewHistory.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        val back_button = findViewById<Button>(R.id.back)
        inputEditText = findViewById(R.id.search_edittext)
        val clearButton = findViewById<ImageView>(R.id.clean)
        val restart_button = findViewById<Button>(R.id.RestartSearch)

        massageNotInternet = findViewById(R.id.massageNotInternet)
        placeholderMessage = findViewById(R.id.placeholderMessageNotInternet)
        messageNotFound = findViewById(R.id.messageNotFound)
        placeholderMessageNotFound = findViewById(R.id.placeholderMessage)

        adapter.tracks = tracks

        back_button.setOnClickListener {
            finish()
        }

        clearButton.setOnClickListener {
            tracks.clear()
            inputEditText.setText("")
            adapter.notifyDataSetChanged()
            hideKeyboard()
        }

        restart_button.setOnClickListener {
            restartButton()
        }

        inputEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = if (s.isNullOrEmpty()) View.GONE else View.VISIBLE
                (s?.toString() ?: "").also { searchQuery = it }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        savedInstanceState?.let {
            val savedText = it.getString("searchText")
            inputEditText.setText(savedText)
        }

        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                restartButton()
                true
            } else {
                false
            }
        }
    }

    private fun restartButton() {
        val searchText = inputEditText.text.toString()
        if (inputEditText.text.isNotEmpty()) {
            apiService.searchTracks(searchText).enqueue(object : Callback<SearchResponse> {
                override fun onResponse(
                    call: Call<SearchResponse>,
                    response: Response<SearchResponse>
                ) {
                    if (response.code() == 200) {
                        tracks.clear()
                        if (response.body()?.results?.isNotEmpty() == true) {
                            tracks.addAll(response.body()?.results!!)
                            adapter.notifyDataSetChanged()
                            massageNotInternet.visibility = View.GONE
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
    private fun showError(text: String, additionalMessage: String) {
        if (text.isNotEmpty()) {
            massageNotInternet.visibility = View.VISIBLE
            placeholderMessage.visibility = View.VISIBLE
            tracks.clear()
            adapter.notifyDataSetChanged()
            placeholderMessage.text = text
            placeholderMessageNotFound.visibility = View.GONE
            if (additionalMessage.isNotEmpty()) {
                Toast.makeText(applicationContext, additionalMessage, Toast.LENGTH_LONG)
                    .show()
            }
        } else {
            massageNotInternet.visibility = View.GONE
            hideKeyboard()
        }
    }

    private fun showMessage(text: String, additionalMessage: String) {
        if (text.isNotEmpty()) {
            messageNotFound.visibility = View.VISIBLE
            placeholderMessageNotFound.visibility = View.VISIBLE
            tracks.clear()
            adapter.notifyDataSetChanged()
            placeholderMessageNotFound.text = text
            if (additionalMessage.isNotEmpty()) {
                Toast.makeText(applicationContext, additionalMessage, Toast.LENGTH_LONG)
                    .show()
            }
        } else {
            messageNotFound.visibility = View.GONE
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
}