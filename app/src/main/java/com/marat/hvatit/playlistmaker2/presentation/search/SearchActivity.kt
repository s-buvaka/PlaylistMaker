package com.marat.hvatit.playlistmaker2.presentation.search

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.marat.hvatit.playlistmaker2.presentation.audioplayer.AudioplayerActivity
import com.marat.hvatit.playlistmaker2.R
import com.marat.hvatit.playlistmaker2.creator.Creator
import com.marat.hvatit.playlistmaker2.domain.api.TrackInteractor
import com.marat.hvatit.playlistmaker2.domain.models.SaveStack
import com.marat.hvatit.playlistmaker2.domain.models.Track


const val EDITTEXT_TEXT = "EDITTEXT_TEXT"
private const val TAG = "SearchActivity"
private lateinit var disconnected: String
private lateinit var nothingToShow: String
private lateinit var allfine: String

class SearchActivity : AppCompatActivity() {

    private var saveEditText: String = "error"


    private val creator: Creator = Creator
    private val interactor = creator.provideTrackInteractor()
    private val gson = creator.provideJsonParser()


    private lateinit var saveSongStack: SaveStack<Track>
    private val appleSongList = ArrayList<Track>()
    private val trackListAdapter = TrackListAdapter(appleSongList)

    private lateinit var placeholder: ImageView
    private lateinit var texterror: TextView
    private lateinit var buttonupdate: ImageButton
    private lateinit var historyText: TextView
    private lateinit var clearHistory: ImageButton
    private lateinit var progressBar: ProgressBar

    companion object {

        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val SEARCH_DEBOUNCE_DELAY = 2000L

        fun getIntent(context: Context, message: String): Intent {
            return Intent(context, SearchActivity::class.java).apply {
                putExtra(TAG, message)
            }
        }
    }

    private var searchText: String? = null
    private val searchRunnable: Runnable = Runnable { searchText?.let { search(it) } }

    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())

    private lateinit var searchActivityState: SearchState


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        val editText = findViewById<EditText>(R.id.editText)
        val buttonBack = findViewById<View>(R.id.back)
        val buttonClear: ImageButton = findViewById(R.id.buttonClear)
        val recyclerSongList = findViewById<RecyclerView>(R.id.songlist)
        //...............................................................
        historyText = findViewById(R.id.messagehistory)
        clearHistory = findViewById(R.id.clearhistory)
        progressBar = findViewById(R.id.progressBar)

        saveSongStack = SaveStack<Track>(applicationContext, 10)
        saveSongStack.addAll(saveSongStack.getItemsFromCache()?.toList() ?: listOf())
        //...............................................................
        placeholder = findViewById(R.id.activity_search_placeholder)
        texterror = findViewById(R.id.activity_search_texterror)
        buttonupdate = findViewById(R.id.activity_search_update)
        disconnected = applicationContext.getString(R.string.act_search_disconnect)
        nothingToShow = applicationContext.getString(R.string.act_search_nothing)
        allfine = applicationContext.getString(R.string.act_search_fine)
        //...............................................................
        recyclerSongList.layoutManager = LinearLayoutManager(this)
        recyclerSongList.adapter = trackListAdapter


        if (savedInstanceState != null) {
            editText.setText(saveEditText)
        }
        //..............................................................
        val simpletextWatcher = object : TextWatcher {
            @RequiresApi(Build.VERSION_CODES.Q)
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Log.e("activityState", "2")
                clearButtonVisibility(s).also { buttonClear.visibility = it }
            }

            override fun afterTextChanged(s: Editable?) {
                Log.e("activityState", "3")
                saveEditText = s.toString()
                if (s.isNullOrEmpty()) {
                    handler.removeCallbacks(searchRunnable)
                    if (saveSongStack.isEmpty()) {
                        searchActivityState = SearchState.ClearState(
                            placeholder,
                            buttonupdate,
                            texterror,
                            progressBar, clearHistory, historyText
                        )
                        activityState(searchActivityState)
                    } else {
                        searchActivityState = SearchState.StartState(
                            placeholder,
                            buttonupdate,
                            texterror,
                            progressBar,
                            clearHistory,
                            historyText
                        )
                        activityState(searchActivityState)
                    }
                } else {
                    searchText = s.toString()
                    Log.e("activityState", "${searchText}")
                    searchDebounce()
                }
            }

        }

        editText.addTextChangedListener(simpletextWatcher)

        buttonBack.setOnClickListener {
            onBackPressed()
        }

        buttonClear.setOnClickListener {
            editText.requestFocus()
            editText.setText("")
            (this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
                editText.windowToken, 0
            )

            if (saveSongStack.isEmpty()) {
                searchActivityState = SearchState.ClearState(
                    placeholder,
                    buttonupdate,
                    texterror,
                    progressBar,
                    clearHistory,
                    historyText
                )
                activityState(searchActivityState)
            } else {
                searchActivityState = SearchState.StartState(
                    placeholder,
                    buttonupdate,
                    texterror,
                    progressBar,
                    clearHistory,
                    historyText
                )
                activityState(searchActivityState)
            }
            trackListAdapter.notifyDataSetChanged()
        }

        editText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                handler.removeCallbacks(searchRunnable)
                search(editText.text.toString())
            }
            false
        }

        editText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && !saveSongStack.isEmpty()) {
                clearHistory.isVisible = true
                historyText.isVisible = true
                searchActivityState = SearchState.StartState(
                    placeholder,
                    buttonupdate,
                    texterror,
                    progressBar,
                    clearHistory,
                    historyText
                )
                activityState(searchActivityState)
            } else {
                clearHistory.isVisible = false
                historyText.isGone = true
            }
        }

        clearHistory.setOnClickListener {
            saveSongStack.clear()
            appleSongList.clear()
            trackListAdapter.notifyDataSetChanged()
            clearHistory.isGone = true
            historyText.isGone = true
            saveSongStack.onStop()
        }

        trackListAdapter.saveTrackListener = TrackListAdapter.SaveTrackListener {
            if (clickDebounce()) {
                addSaveSongs(it)
                AudioplayerActivity.getIntent(this@SearchActivity, this.getString(R.string.android))
                    .apply {
                        putExtra("Track", gson.objectToJson(it)/*toJson(it)*/)
                        startActivity(this)
                    }
            }
        }

        buttonupdate.setOnClickListener {
            search(editText.text.toString())
        }


    }

    override fun onStop() {
        super.onStop()
        handler.removeCallbacks(searchRunnable)
        saveSongStack.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(EDITTEXT_TEXT, saveEditText)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        saveEditText = savedInstanceState.getString(EDITTEXT_TEXT).toString()
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            ImageButton.GONE
        } else {
            ImageButton.VISIBLE
        }
    }

    private fun search(text: String) {
        searchActivityState = SearchState.Download(
            placeholder,
            buttonupdate,
            texterror,
            progressBar,
            clearHistory,
            historyText
        )
        activityState(searchActivityState)
        interactor.searchTrack(text, object : TrackInteractor.TrackConsumer {
            override fun consume(foundTrack: List<Track>) {
                runOnUiThread {
                    appleSongList.clear()
                    searchActivityState = SearchState.ClearState(
                        placeholder,
                        buttonupdate,
                        texterror,
                        progressBar,
                        clearHistory,
                        historyText
                    )
                    activityState(searchActivityState)
                    appleSongList.addAll(foundTrack)
                    trackListAdapter.notifyDataSetChanged()
                    if (foundTrack.isNullOrEmpty()) {
                        searchActivityState = SearchState.NothingToShow(
                            placeholder,
                            buttonupdate,
                            texterror,
                            progressBar,
                            clearHistory,
                            historyText,
                            nothingToShow
                        )
                        activityState(searchActivityState)
                    }
                }
            }
        })

    }

    private fun activityState(state: SearchState) {
        when (state) {
            is SearchState.AllFine -> {
                state.setState()
            }

            is SearchState.ClearState -> {
                appleSongList.clear()
                state.setState()
            }

            is SearchState.Disconnected -> {
                appleSongList.clear()
                state.setState()
            }

            is SearchState.Download -> {
                appleSongList.clear()
                state.setState()
            }

            is SearchState.NothingToShow -> {
                appleSongList.clear()
                state.setState()
            }

            is SearchState.StartState -> {
                setSavedTracks()
                if (!saveSongStack.isEmpty()) {
                    clearHistory.isVisible = true
                    historyText.isVisible = true
                }
                state.setState()
            }
        }
        trackListAdapter.notifyDataSetChanged()
    }

    private fun setSavedTracks() {
        appleSongList.clear()
        appleSongList.addAll(saveSongStack)
        trackListAdapter.notifyDataSetChanged()

    }

    private fun addSaveSongs(item: Track) {
        if (saveSongStack.searchId(item)) {
            saveSongStack.remove(item)
        }
        saveSongStack.pushElement(item)
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

}