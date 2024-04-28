package com.marat.hvatit.playlistmaker2.presentation.audioplayer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.marat.hvatit.playlistmaker2.domain.api.AudioPlayerInteractor

class AudioViewModel(private val interactor: AudioPlayerInteractor) : ViewModel() {

    private var playerState: MediaPlayerState = MediaPlayerState.Default
    private val playingState : MediaPlayerState.Playing = MediaPlayerState.Playing(interactor.updateTimer())
    private var loadingLiveData = MutableLiveData(playerState)


    fun getLoadingLiveData(): LiveData<MediaPlayerState> = loadingLiveData

    fun playbackControl(){
        loadingLiveData.postValue(interactor.playbackControl())
        //playerState = interactor.playbackControl()
    }

    fun refreshTime(){
        //playingState.copy(interactor.updateTimer())
        loadingLiveData.postValue(playingState)

        //loadingLiveData.postValue(playingState.copy(interactor.updateTimer()))
        //loadingLiveData.postValue(playingState)
        //loadingLiveData.postValue(MediaPlayerState.Playing(this.interactor.updateTimer()))
    }

    fun stopPlayer() {
        interactor.stopPlayer()
        loadingLiveData.postValue(MediaPlayerState.Paused)
    }

    fun destroyPlayer() {
        interactor.destroyPlayer()
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