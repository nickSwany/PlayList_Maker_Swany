package search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pl_market.R
import com.example.pl_market.Track
import search.classes.TrackAdapter

class SearchActivity : AppCompatActivity() {

    private lateinit var inputEditText: EditText

    companion object {
        var searchQuery = ""
        const val SEARCH_STRING = "SEARCH_STRING"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val trackAdapter = TrackAdapter(listOf(
            Track("Smells like Tenn Spirit", "Nirvana", "5:01", getString(R.string.nirvana_art)),
            Track("Billie Jean", "Michael Jackson", "4:35", getString(R.string.jackson_art)),
            Track("Stayin' Alive", "Bee Gees", "4:10", getString(R.string.bee_art)),
            Track("Whole Lotta Love", "Led Zeppelin", "5:33", getString(R.string.led_art)),
            Track("Sweet Child O'Mine", "Guns N' Roses", "5:03", getString(R.string.guns_art)),
        ))

        val rcViewHistory = findViewById<RecyclerView>(R.id.rcViewHistory)
        rcViewHistory.adapter = trackAdapter
        rcViewHistory.layoutManager = LinearLayoutManager(this)

        val back_button = findViewById<Button>(R.id.back)
        inputEditText = findViewById(R.id.search_edittext)
        val clearButton = findViewById<ImageView>(R.id.clean)

        back_button.setOnClickListener {
            finish()
        }

        clearButton.setOnClickListener {
            inputEditText.setText("")
            hideKeyboard()
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




