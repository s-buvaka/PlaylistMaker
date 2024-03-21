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
import com.google.gson.Gson
import com.marat.hvatit.playlistmaker2.data.network.AppleMusicAPI
import com.marat.hvatit.playlistmaker2.data.network.AppleSong
import com.marat.hvatit.playlistmaker2.data.network.AppleSongResponce
import com.marat.hvatit.playlistmaker2.presentation.audioplayer.AudioplayerActivity
import com.marat.hvatit.playlistmaker2.R
import com.marat.hvatit.playlistmaker2.domain.models.SaveStack
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val EDITTEXT_TEXT = "EDITTEXT_TEXT"
private const val TAG = "SearchActivity"
private lateinit var disconnected: String
private lateinit var nothingToShow: String
private lateinit var allfine: String

enum class SearchActivityState {
    DISCONNECTED, NOTHINGTOSHOW, ALLFINE, STARTSTATE, CLEARSTATE, DOWNLOAD
}

class SearchActivity : AppCompatActivity() {

    private var saveEditText: String = "error"

    private val appleBaseUrl = "https://itunes.apple.com"

    private val gson: Gson = Gson()

    private val retrofit =
        Retrofit.Builder().baseUrl(appleBaseUrl).addConverterFactory(GsonConverterFactory.create())
            .build()

    private val appleService = retrofit.create(AppleMusicAPI::class.java)


    private lateinit var saveSongStack: SaveStack<AppleSong>
    private val appleSongList = ArrayList<AppleSong>()
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        Log.e("activityState", "OnCreate")
        Log.e("activityState", "${retrofit.hashCode()}")
        Log.e("activityState", "${appleService.hashCode()}")
        val editText = findViewById<EditText>(R.id.editText)
        val buttonBack = findViewById<View>(R.id.back)
        val buttonClear: ImageButton = findViewById(R.id.buttonClear)
        val recyclerSongList = findViewById<RecyclerView>(R.id.songlist)
        //...............................................................
        historyText = findViewById(R.id.messagehistory)
        clearHistory = findViewById(R.id.clearhistory)
        progressBar = findViewById(R.id.progressBar)

        saveSongStack = SaveStack<AppleSong>(applicationContext, 10)
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
                        activityState(SearchActivityState.CLEARSTATE)
                    } else {
                        activityState(SearchActivityState.STARTSTATE)
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
                editText.windowToken,
                0
            )
            //appleSongList.clear()
            if (saveSongStack.isEmpty()) {
                activityState(SearchActivityState.CLEARSTATE)
            } else {
                activityState(SearchActivityState.STARTSTATE)
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
                activityState(SearchActivityState.STARTSTATE)
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
                        putExtra("Track", gson.toJson(it))
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
        activityState(SearchActivityState.DOWNLOAD)
        //appleService.search(text).execute()
        appleService.search(text)
            .enqueue(object : Callback<AppleSongResponce> {
                override fun onResponse(
                    call: Call<AppleSongResponce>,
                    responce: Response<AppleSongResponce>
                ) {
                    when (responce.code()) {
                        200 -> {
                            if (responce.body()?.results?.isEmpty() == false) {
                                appleSongList.clear()
                                activityState(SearchActivityState.CLEARSTATE)
                                appleSongList.addAll(responce.body()!!.results)
                                Log.e("responce", responce.body()?.results.toString())
                            } else {
                                activityState(SearchActivityState.NOTHINGTOSHOW)
                            }
                        }

                        else -> {
                            activityState(SearchActivityState.DISCONNECTED)
                        }

                    }
                }

                override fun onFailure(call: Call<AppleSongResponce>, t: Throwable) {
                    activityState(SearchActivityState.DISCONNECTED)
                }
            })
    }

    private fun activityState(state: SearchActivityState) {
        //переделать на манер sealedClass
        when (state) {
            SearchActivityState.DISCONNECTED -> {
                appleSongList.clear()
                placeholder.setImageResource(R.drawable.disconnect_problem)
                placeholder.isVisible = true
                buttonupdate.isVisible = true
                texterror.text = disconnected
                texterror.isVisible = true
                clearHistory.isVisible = false
                historyText.isVisible = false
                progressBar.isVisible = false
                Log.e("activityState", "DISCONNECTED")
            }

            SearchActivityState.NOTHINGTOSHOW -> {
                appleSongList.clear()
                placeholder.setImageResource(R.drawable.nothing_problem)
                placeholder.isVisible = true
                texterror.text = nothingToShow
                texterror.isVisible = true
                clearHistory.isVisible = false
                historyText.isVisible = false
                progressBar.isVisible = false
                Log.e("activityState", "NOTHINGTOSHOW")
            }

            SearchActivityState.ALLFINE -> {
                buttonupdate.isVisible = false
                placeholder.isVisible = false
                texterror.isVisible = false
                progressBar.isVisible = false
                Log.e("activityState", "ALLFINE")
            }

            SearchActivityState.STARTSTATE -> {
                progressBar.isVisible = false
                buttonupdate.isVisible = false
                placeholder.isVisible = false
                texterror.isVisible = false
                getSaveSongs()
                if (!saveSongStack.isEmpty()) {
                    clearHistory.isVisible = true
                    historyText.isVisible = true
                }
                Log.e("activityState", "STARTSTATE")
            }

            SearchActivityState.CLEARSTATE -> {
                appleSongList.clear()
                progressBar.isVisible = false
                clearHistory.isGone = true
                historyText.isGone = true
                buttonupdate.isVisible = false
                placeholder.isVisible = false
                texterror.isVisible = false
                trackListAdapter.notifyDataSetChanged()
                Log.e("activityState", "CLEARSTATE")
            }

            SearchActivityState.DOWNLOAD -> {
                appleSongList.clear()
                progressBar.isVisible = true
                clearHistory.isGone = true
                historyText.isGone = true
                buttonupdate.isVisible = false
                placeholder.isVisible = false
                texterror.isVisible = false
                Log.e("activityState", "DOWNLOAD")
            }
        }
        trackListAdapter.notifyDataSetChanged()
    }

    private fun getSaveSongs() {//изменить название
        appleSongList.clear()
        appleSongList.addAll(saveSongStack)
        trackListAdapter.notifyDataSetChanged()

    }

    private fun addSaveSongs(item: AppleSong) {
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