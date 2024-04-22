package com.marat.hvatit.playlistmaker2.presentation.audioplayer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.marat.hvatit.playlistmaker2.domain.api.AudioPlayerInteractor

class AudioViewModel(private val interactor: AudioPlayerInteractor) : ViewModel() {

    fun playbackControl(): MediaPlayerState{
        return interactor.playbackControl()
    }

    fun stopPlayer(){
        interactor.stopPlayer()
    }

    fun destroyPlayer(){
        interactor.destroyPlayer()
    }

    fun updateTimer() : String{
        return interactor.updateTimer()
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