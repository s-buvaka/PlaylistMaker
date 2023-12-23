package com.marat.hvatit.playlistmaker2

import android.content.Context
import android.content.Intent
import android.media.Image
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.marat.hvatit.playlistmaker2.adapters.TrackListAdapter
import com.marat.hvatit.playlistmaker2.models.Track
import com.google.gson.Gson
import com.marat.hvatit.playlistmaker2.service.AppleMusicAPI
import com.marat.hvatit.playlistmaker2.service.AppleSong
import com.marat.hvatit.playlistmaker2.service.AppleSongResponce
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Query

const val EDITTEXT_TEXT = "EDITTEXT_TEXT"
private const val TAG = "SearchActivity"
private lateinit var disconnected: String
private lateinit var nothingToShow: String
private lateinit var allfine: String

enum class SearchActivityState(val value: String) {
    DISCONNECTED(""), NOTHINGTOSHOW(""), ALLFINE("")
}

class SearchActivity : AppCompatActivity() {

    private var saveEditText: String = "error"

    private val appleBaseUrl = "https://itunes.apple.com"

    private val retrofit =
        Retrofit.Builder().baseUrl(appleBaseUrl).addConverterFactory(GsonConverterFactory.create())
            .build()

    private val appleService = retrofit.create(AppleMusicAPI::class.java)

    private var appleSongList = ArrayList<AppleSong>()
    private var trackListAdapter = TrackListAdapter(appleSongList)

    var placeholder: ImageView? = null
    var texterror: TextView? = null
    var buttonupdate: ImageButton? = null
    //Они мне тут не нравятся, как правильно с ними поступить и почему?)

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
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //empty
                clearButtonVisibility(s).also { buttonClear.visibility = it }
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

        buttonupdate?.setOnClickListener {
            search(editText.text.toString())
        }

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
                                activityState(SearchActivityState.ALLFINE)
                                appleSongList.addAll(responce.body()!!.results)
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
        }
    }

    private fun placeholderHandler(placeholderMessage: String) {
        when (placeholderMessage) {
            nothingToShow -> {
                appleSongList.clear()
                placeholder?.setImageResource(R.drawable.nothing_problem)
                placeholder?.isVisible = true
                texterror?.text = placeholderMessage
                texterror?.isVisible = true
            }

            disconnected -> {
                appleSongList.clear()
                placeholder?.setImageResource(R.drawable.disconnect_problem)
                placeholder?.isVisible = true
                buttonupdate?.isVisible = true
                texterror?.text = placeholderMessage
                texterror?.isVisible = true
            }

            allfine -> {
                buttonupdate?.isVisible = false
                placeholder?.isVisible = false
                texterror?.isVisible = false
            }
        }
        trackListAdapter.notifyDataSetChanged()
        //Много вопросов)))
    }
}