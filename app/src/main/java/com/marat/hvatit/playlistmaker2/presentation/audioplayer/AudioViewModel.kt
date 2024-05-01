package com.marat.hvatit.playlistmaker2.presentation.audioplayer

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.marat.hvatit.playlistmaker2.domain.api.AudioPlayerInteractor

class AudioViewModel(private val interactor: AudioPlayerInteractor) : ViewModel() {

    private var playerState: MediaPlayerState = MediaPlayerState.Default
    private var currentState: MediaPlayerState? = MediaPlayerState.Default
    private var loadingLiveData = MutableLiveData(playerState)

    private val handler = Handler(Looper.getMainLooper())
    private val timerRunnable: Runnable = Runnable { updateTimer() }


    fun getLoadingLiveData(): LiveData<MediaPlayerState> = loadingLiveData

    fun playbackControl() {
        currentState = interactor.playbackControl()
        loadingLiveData.postValue(currentState)
        if (currentState is MediaPlayerState.Playing) {
            updateTimer()
        } else {
            stopTimer()
        }
    }

    private fun refreshTime() {
        loadingLiveData.postValue(MediaPlayerState.Playing(interactor.updateTimer()))
    }

    fun stopPlayer() {
        stopTimer()
        interactor.stopPlayer()
        loadingLiveData.postValue(MediaPlayerState.Paused)
    }

    fun destroyPlayer() {
        stopTimer()
        interactor.destroyPlayer()
    }

    fun trackIsDone(){
        stopTimer()
        loadingLiveData.postValue(MediaPlayerState.Paused)
    }

    private fun updateTimer() {
        Log.e("MediaState", "updateTimer")
        refreshTime()
        handler.postDelayed(timerRunnable, 1000L)
    }

    private fun stopTimer() {
        handler.removeCallbacks(timerRunnable)
    }
    companion object {
        fun getViewModelFactory(interactor: AudioPlayerInteractor): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    AudioViewModel(interactor)
                }
            }
    }

}