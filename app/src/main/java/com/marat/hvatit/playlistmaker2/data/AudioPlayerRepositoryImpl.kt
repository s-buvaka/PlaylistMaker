package com.marat.hvatit.playlistmaker2.data

import android.media.MediaPlayer
import com.marat.hvatit.playlistmaker2.domain.api.AudioPlayerCallback
import com.marat.hvatit.playlistmaker2.domain.api.AudioPlayerRepository
import com.marat.hvatit.playlistmaker2.presentation.audioplayer.MediaPlayerState
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerRepositoryImpl(private val priviewUrl: String, private val activityCallBack: AudioPlayerCallback) :
    AudioPlayerRepository {


    private var mediaPlayer = MediaPlayer()
    private var playerState = MediaPlayerState.STATE_DEFAULT
    override fun stateControl(): MediaPlayerState {
        when (playerState) {
            MediaPlayerState.STATE_PLAYING -> {
                pausePlayer()
            }

            MediaPlayerState.STATE_PAUSED -> {
                startPlayer()
            }

            MediaPlayerState.STATE_DEFAULT -> {
                preparePlayer(priviewUrl)
            }

            MediaPlayerState.STATE_PREPARED -> {
                startPlayer()
            }
        }
        return playerState
    }

    private fun preparePlayer(url: String) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState = MediaPlayerState.STATE_PREPARED
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        playerState = MediaPlayerState.STATE_PLAYING
        mediaPlayer.setOnCompletionListener {
            mediaPlayer.seekTo(0)
            playerState = MediaPlayerState.STATE_PREPARED
            activityCallBack.trackIsDone()
        }
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        playerState = MediaPlayerState.STATE_PAUSED
    }

    override fun getCurrentTime(): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition)
    }

    override fun destroyActivity() {
        mediaPlayer.release()
    }

    override fun pauseActivity() {
        pausePlayer()
    }
}