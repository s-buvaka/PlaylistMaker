package com.marat.hvatit.playlistmaker2

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import com.marat.hvatit.playlistmaker2.service.AppleSong
import java.text.SimpleDateFormat
import java.util.Locale


private const val TAG = "AudioplayerActivity"

enum class MediaPlayerState {
    STATE_DEFAULT, STATE_PREPARED, STATE_PLAYING, STATE_PAUSED
}

class AudioplayerActivity : AppCompatActivity() {

    private lateinit var intent: Intent
    private val gson: Gson = Gson()
    private val simpleDateFormat: SimpleDateFormat =
        SimpleDateFormat("mm:ss", Locale.getDefault())


    private lateinit var actplayerCover: ImageView
    private val roundedCornersImage: Int = 10

    private lateinit var countryvalue: TextView
    private lateinit var genrevalue: TextView
    private lateinit var yearvalue: TextView
    private lateinit var albumvalue: TextView
    private lateinit var durationvalue: TextView

    private lateinit var artistName: TextView
    private lateinit var trackName: TextView

    private lateinit var buttonPlay: ImageButton
    private lateinit var priviewTimer: TextView

    private var playerState = MediaPlayerState.STATE_DEFAULT
    private var mediaPlayer = MediaPlayer()
    private lateinit var priviewUrl: String
    private val handler = Handler(Looper.getMainLooper())
    private val timerRunnable: Runnable = Runnable { updateTimer() }


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
        buttonPlay = findViewById(R.id.actplayer_buttonplay)
        priviewTimer = findViewById(R.id.actplayer_tracktime)

        //..............................................................
        val buttonBack = findViewById<View>(R.id.back)
        buttonBack.setOnClickListener {
            onBackPressed()
        }
        intent = getIntent()

        val song = intent.getStringExtra("Track")
        val result: AppleSong = gson.fromJson(song, AppleSong::class.java)
        priviewUrl = result.priviewUrl
        Log.e("$TAG", result.toString())
        setTextContent(result)
        buttonPlay.isEnabled = false
        playbackControl()
        buttonPlay.setOnClickListener {
            playbackControl()
        }

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
        yearvalue.setText(song.year.substring(0, song.year.indexOf("-")))
        albumvalue.setText(song.album)
        priviewTimer.text = "00:30"
    }

    private fun preparePlayer(url: String) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            buttonPlay.isEnabled = true
            playerState = MediaPlayerState.STATE_PREPARED
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        updateTimer()
        playerState = MediaPlayerState.STATE_PLAYING
        buttonPlay.setBackgroundResource(R.drawable.button_stop)
        mediaPlayer.setOnCompletionListener {
            stopTimer()
            priviewTimer.text = "00:00"
            mediaPlayer.seekTo(0)
            buttonPlay.setBackgroundResource(R.drawable.button_play)
            playerState = MediaPlayerState.STATE_PREPARED

        }
    }

    private fun pausePlayer() {
        stopTimer()
        mediaPlayer.pause()
        playerState = MediaPlayerState.STATE_PAUSED
        buttonPlay.setBackgroundResource(R.drawable.button_play)
    }

    private fun playbackControl() {
        when (playerState) {
            MediaPlayerState.STATE_PLAYING -> {
                pausePlayer()
            }
            MediaPlayerState.STATE_PAUSED ->{
                startPlayer()
            }

            MediaPlayerState.STATE_DEFAULT -> {
                preparePlayer(priviewUrl)
            }

            MediaPlayerState.STATE_PREPARED -> {
                startPlayer()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(timerRunnable)
        mediaPlayer.release()
    }

    private fun updateTimer() {
        stopTimer()
        priviewTimer.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition)
        handler.postDelayed(timerRunnable, 1000L)
        //Log.e("timerAudio", "Timer:${mediaPlayer.currentPosition/1000}")
        //Log.e("StateAudioPlayer","${mediaPlayer.currentPosition/1000}+/+${priviewTimer.text}")
        //Log.e("AudioPlayerHash",mediaPlayer.hashCode().toString())
    }

    private fun stopTimer(){
        handler.removeCallbacks(timerRunnable)
    }

}