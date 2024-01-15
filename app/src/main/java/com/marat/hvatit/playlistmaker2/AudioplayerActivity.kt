package com.marat.hvatit.playlistmaker2

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import com.marat.hvatit.playlistmaker2.service.AppleSong
import java.text.SimpleDateFormat
import java.util.Locale


private const val TAG = "AudioplayerActivity"

class AudioplayerActivity : AppCompatActivity() {

    private lateinit var intent: Intent
    private val gson: Gson = Gson()
    private val simpleDateFormat: SimpleDateFormat =
        SimpleDateFormat("mm:ss", Locale.getDefault())

    private val dateTimeFormat: SimpleDateFormat =
        SimpleDateFormat("yyyy", Locale.getDefault())


    private lateinit var actplayerCover: ImageView
    private val roundedCornersImage: Int = 10

    private lateinit var countryvalue: TextView
    private lateinit var genrevalue: TextView
    private lateinit var yearvalue: TextView
    private lateinit var albumvalue: TextView
    private lateinit var durationvalue: TextView

    private lateinit var artistName: TextView
    private lateinit var trackName: TextView

    companion object {

        fun getIntent(context: Context, message: String): Intent {
            return Intent(context, AudioplayerActivity::class.java).apply {
                putExtra(TAG, message)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audioplayer)

        actplayerCover = findViewById(R.id.actplayer_cover)
        artistName = findViewById(R.id.actplayer_artist_name)
        trackName = findViewById(R.id.actplayer_track_name)
        //..............................................................
        countryvalue = findViewById(R.id.actplayer_countryvalue)
        genrevalue = findViewById(R.id.actplayer_genrevalue)
        yearvalue = findViewById(R.id.actplayer_yearvalue)
        albumvalue = findViewById(R.id.actplayer_albumvalue)
        durationvalue = findViewById(R.id.actplayer_durationvalue)

        //..............................................................
        val buttonBack = findViewById<View>(R.id.back)
        buttonBack.setOnClickListener {
            onBackPressed()
        }
        intent = getIntent()

        val song = intent.getStringExtra("Track")
        val result: AppleSong = gson.fromJson(song, AppleSong::class.java)
        Log.e("$TAG", result.toString())
        setTextContent(result)


    }

    private fun setTextContent(song: AppleSong) {
        Glide.with(applicationContext)
            .load(song.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"))
            .placeholder(R.drawable.placeholder)
            .transform(RoundedCorners(roundedCornersImage))
            .into(actplayerCover)
        durationvalue.setText(simpleDateFormat.format(song.trackTimeMills.toLong()))
        artistName.setText(song.artistName)
        trackName.setText(song.trackName)
        countryvalue.setText(song.country)
        genrevalue.setText(song.genre)
        yearvalue.setText(song.year.substring(0,song.year.indexOf("-")))
        albumvalue.setText(song.album)


    }
}