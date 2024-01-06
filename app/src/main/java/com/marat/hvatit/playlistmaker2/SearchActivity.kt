package com.marat.hvatit.playlistmaker2

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.marat.hvatit.playlistmaker2.adapters.TrackListAdapter
import com.marat.hvatit.playlistmaker2.databinding.ActivityMainBinding
import com.marat.hvatit.playlistmaker2.datasource.SaveStack
import com.marat.hvatit.playlistmaker2.service.AppleMusicAPI
import com.marat.hvatit.playlistmaker2.service.AppleSong
import com.marat.hvatit.playlistmaker2.service.AppleSongResponce
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
    DISCONNECTED, NOTHINGTOSHOW, ALLFINE, STARTSTATE, CLEARSTATE
}

class SearchActivity : AppCompatActivity() {

    private var saveEditText: String = "error"
    private lateinit var displayMetrics: DisplayMetrics
    private lateinit var defaultRecyclerParams: ViewGroup.LayoutParams
    private lateinit var contRecyclerView: LinearLayoutCompat

    private val appleBaseUrl = "https://itunes.apple.com"

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

    companion object {

        fun getIntent(context: Context, message: String): Intent {
            return Intent(context, SearchActivity::class.java).apply {
                putExtra(TAG, message)
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        val editText = findViewById<EditText>(R.id.editText)
        val buttonBack = findViewById<View>(R.id.back)
        val buttonClear: ImageButton = findViewById(R.id.buttonClear)
        val recyclerSongList = findViewById<RecyclerView>(R.id.songlist)
        defaultRecyclerParams = recyclerSongList.layoutParams

        //...............................................................
        historyText = findViewById(R.id.messagehistory)
        clearHistory = findViewById(R.id.clearhistory)

        saveSongStack = SaveStack<AppleSong>(applicationContext, 10)
        saveSongStack.addAll(saveSongStack.getItemsFromCache()?.toList() ?: listOf())
        displayMetrics = resources.displayMetrics
        //...............................................................
        placeholder = findViewById(R.id.activity_search_placeholder)
        texterror = findViewById(R.id.activity_search_texterror)
        buttonupdate = findViewById(R.id.activity_search_update)
        disconnected = applicationContext.getString(R.string.act_search_disconnect)
        nothingToShow = applicationContext.getString(R.string.act_search_nothing)
        allfine = applicationContext.getString(R.string.act_search_fine)
        //...............................................................
        contRecyclerView = findViewById(R.id.ll_recyclercont)
        recyclerSongList.layoutManager = LinearLayoutManager(this)
        recyclerSongList.adapter = trackListAdapter

        if (savedInstanceState != null) {
            editText.setText(saveEditText)
        }
        //..............................................................
        val simpletextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //empty
                clearButtonVisibility(s).also { buttonClear.visibility = it }
                activityState(SearchActivityState.CLEARSTATE)

            }

            override fun afterTextChanged(s: Editable?) {
                //empty
                saveEditText = s.toString()
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
            appleSongList.clear()
            trackListAdapter.notifyDataSetChanged()
        }

        editText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                search(editText.text.toString())
            }
            false
        }

        editText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus&&!saveSongStack.isEmpty()) {
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
            addSaveSongs(it)
        }

        buttonupdate.setOnClickListener {
            search(editText.text.toString())
        }

    }

    override fun onStop() {
        super.onStop()
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
                                Log.e("notify1", "1")
                                activityState(SearchActivityState.ALLFINE)
                                appleSongList.addAll(responce.body()!!.results)
                                Log.e("notify1", "3")
                                Log.e("response", appleSongList.toString())
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
        when (state) {
            SearchActivityState.DISCONNECTED -> placeholderHandler(disconnected)
            SearchActivityState.NOTHINGTOSHOW -> placeholderHandler(nothingToShow)
            SearchActivityState.ALLFINE -> placeholderHandler(allfine)
            SearchActivityState.STARTSTATE -> {
                getSaveSongs()
                clearHistory.isVisible = true
                historyText.isVisible = true
            }

            SearchActivityState.CLEARSTATE -> {
                appleSongList.clear()
                clearHistory.isGone = true
                historyText.isGone = true
                trackListAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun placeholderHandler(placeholderMessage: String) {
        when (placeholderMessage) {
            nothingToShow -> {
                appleSongList.clear()
                placeholder.setImageResource(R.drawable.nothing_problem)
                placeholder.isVisible = true
                texterror.text = placeholderMessage
                texterror.isVisible = true
            }

            disconnected -> {
                appleSongList.clear()
                placeholder.setImageResource(R.drawable.disconnect_problem)
                placeholder.isVisible = true
                buttonupdate.isVisible = true
                texterror.text = placeholderMessage
                texterror.isVisible = true
            }

            allfine -> {
                buttonupdate.isVisible = false
                placeholder.isVisible = false
                texterror.isVisible = false
            }
        }
        clearHistory.isGone = true
        historyText.isGone = true
        trackListAdapter.notifyDataSetChanged()
    }

    private fun getSaveSongs() {
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

}