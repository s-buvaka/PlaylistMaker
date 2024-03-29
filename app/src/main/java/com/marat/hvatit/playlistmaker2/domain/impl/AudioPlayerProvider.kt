package com.marat.hvatit.playlistmaker2.domain.impl


import com.marat.hvatit.playlistmaker2.domain.api.AudioPlayerInteractor
import com.marat.hvatit.playlistmaker2.presentation.audioplayer.MediaPlayerState

class AudioPlayerProvider(private val audioPlayerImpl: AudioPlayerInteractor)  {

     fun playbackControl(): MediaPlayerState {
        return audioPlayerImpl.stateControl()
    }

     fun updateTimer(): String {
        return audioPlayerImpl.getCurrentTime()
    }

     fun destroyPlayer() {
        audioPlayerImpl.destroyActivity()
    }

     fun stopPlayer(){
        audioPlayerImpl.pauseActivity()
    }

}