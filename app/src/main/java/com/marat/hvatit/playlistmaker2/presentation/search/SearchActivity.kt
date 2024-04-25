package com.marat.hvatit.playlistmaker2.presentation.search

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
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
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.marat.hvatit.playlistmaker2.R
import com.marat.hvatit.playlistmaker2.creator.Creator
import com.marat.hvatit.playlistmaker2.domain.models.SaveStack
import com.marat.hvatit.playlistmaker2.domain.models.Track
import com.marat.hvatit.playlistmaker2.presentation.audioplayer.AudioplayerActivity


const val EDITTEXT_TEXT = "EDITTEXT_TEXT"
private const val TAG = "SearchActivity"

class SearchActivity : AppCompatActivity() {

    private var saveEditText: String = "error"


    private val creator: Creator = Creator
    private val interactor = creator.provideTrackInteractor()
    private val gson = creator.provideJsonParser()
    private lateinit var saveSongStack: SaveStack<Track>


    private val trackListAdapter = TrackListAdapter()

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
    private val searchRunnable: Runnable =
        Runnable { searchText?.let { viewModel.search(it) } }

    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())

    private lateinit var viewModel: SearchViewModel


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
        //...............................................................
        placeholder = findViewById(R.id.activity_search_placeholder)
        texterror = findViewById(R.id.activity_search_texterror)
        buttonupdate = findViewById(R.id.activity_search_update)
        //...............................................................
        recyclerSongList.layoutManager = LinearLayoutManager(this)
        recyclerSongList.adapter = trackListAdapter

        saveSongStack = creator.provideSaveStack(10)
        viewModel = ViewModelProvider(
            this,
            SearchViewModel.getViewModelFactory(interactor, saveSongStack)
        )[SearchViewModel::class.java]
        //get(SearchViewModel::class.java)

        viewModel.getLoadingLiveData().observe(this) { searchState ->
            runOnUiThread {
                onState(searchState)
            }
        }

        if (savedInstanceState != null) {
            editText.setText(saveEditText)
        }
        //..............................................................

        editText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && !viewModel.isEmptyStack()) {
                trackListAdapter.update(viewModel.getSaveTracks())
                viewModel.changeState(SearchState.StartState)
            } else {
                viewModel.changeState(SearchState.ClearState)
            }
        }

        val simpletextWatcher = object : TextWatcher {
            @RequiresApi(Build.VERSION_CODES.Q)
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButtonVisibility(s).also { buttonClear.visibility = it }
            }

            override fun afterTextChanged(s: Editable?) {
                saveEditText = s.toString()
                if (s.isNullOrEmpty()) {
                    handler.removeCallbacks(searchRunnable)
                    if (viewModel.isEmptyStack()) {
                        viewModel.changeState(SearchState.ClearState)
                    } else {
                        viewModel.changeState(SearchState.StartState)
                    }
                } else {
                    searchText = s.toString()
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
            if (viewModel.isEmptyStack()) {
                viewModel.changeState(SearchState.ClearState)
            } else {
                viewModel.changeState(SearchState.StartState)
            }
            trackListAdapter.notifyDataSetChanged()
        }

        editText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                handler.removeCallbacks(searchRunnable)
                viewModel.search(editText.text.toString())
            }
            false
        }

        clearHistory.setOnClickListener {
            clearHistory.isGone = true
            historyText.isGone = true

            viewModel.clearSaveStack()
            trackListAdapter.update(emptyList())
            trackListAdapter.notifyDataSetChanged()
        }

        trackListAdapter.saveTrackListener = TrackListAdapter.SaveTrackListener {
            if (clickDebounce()) {
                viewModel.addSaveSongs(it)
                trackListAdapter.update(viewModel.getSaveTracks())
                trackListAdapter.notifyDataSetChanged()
                AudioplayerActivity.getIntent(this@SearchActivity, this.getString(R.string.android))
                    .apply {
                        putExtra("Track", gson.objectToJson(it)/*toJson(it)*/)
                        startActivity(this)
                    }
            }
        }

        buttonupdate.setOnClickListener {
            viewModel.search(editText.text.toString())
        }


    }

    private fun onState(searchState: SearchState) {
        when (searchState) {
            is SearchState.AllFine -> {
                buttonupdate.isVisible = false
                placeholder.isVisible = false
                texterror.isVisible = false
                progressBar.isVisible = false
            }

            is SearchState.ClearState -> {
                trackListAdapter.update(emptyList())
                buttonupdate.isVisible = false
                placeholder.isVisible = false
                texterror.isVisible = false

                clearHistory.isVisible = false
                historyText.isVisible = false
                progressBar.isVisible = false
            }

            is SearchState.Data -> {
                trackListAdapter.update(searchState.foundTrack)

                placeholder.isVisible = false
                buttonupdate.isVisible = false
                texterror.isVisible = false
                progressBar.isVisible = false

                clearHistory.isVisible = false
                historyText.isVisible = false
            }

            is SearchState.Disconnected -> {
                trackListAdapter.update(emptyList())
                placeholder.setImageResource(R.drawable.disconnect_problem)
                placeholder.isVisible = true
                buttonupdate.isVisible = true
                texterror.text = applicationContext.getString(searchState.message)
                texterror.isVisible = true


                clearHistory.isVisible = false
                historyText.isVisible = false
                progressBar.isVisible = false
            }

            is SearchState.Download -> {
                trackListAdapter.update(emptyList())
                buttonupdate.isVisible = false
                placeholder.isVisible = false
                texterror.isVisible = false

                clearHistory.isVisible = false
                historyText.isVisible = false
                progressBar.isVisible = true
            }

            is SearchState.NothingToShow -> {
                trackListAdapter.update(emptyList())
                placeholder.setImageResource(R.drawable.nothing_problem)
                placeholder.isVisible = true
                texterror.text = applicationContext.getString(searchState.message)
                texterror.isVisible = true

                clearHistory.isVisible = false
                historyText.isVisible = false
                progressBar.isVisible = false
            }

            is SearchState.StartState -> {
                placeholder.isVisible = false
                buttonupdate.isVisible = false
                texterror.isVisible = false
                progressBar.isVisible = false
                getSavedTracksAct()

                clearHistory.isVisible = true
                historyText.isVisible = true
            }
        }
        trackListAdapter.notifyDataSetChanged()
    }

    override fun onStop() {
        super.onStop()
        handler.removeCallbacks(searchRunnable)
        viewModel.setSaveTracks()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.setSaveTracks()
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

    private fun getSavedTracksAct() {
        if (viewModel.isEmptyStack()) {
            clearHistory.isVisible = false
            historyText.isVisible = false
        }
        clearHistory.isVisible = true
        historyText.isVisible = true
        trackListAdapter.update(viewModel.getSaveTracks())
        trackListAdapter.notifyDataSetChanged()
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