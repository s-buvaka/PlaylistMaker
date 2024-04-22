package com.marat.hvatit.playlistmaker2.domain.impl


import com.marat.hvatit.playlistmaker2.domain.api.AudioPlayerInteractor
import com.marat.hvatit.playlistmaker2.domain.api.AudioPlayerRepository
import com.marat.hvatit.playlistmaker2.presentation.audioplayer.MediaPlayerState

class AudioPlayerInteractorImpl(private val audioPlayerImpl: AudioPlayerRepository) :
    AudioPlayerInteractor {

    override fun playbackControl(): MediaPlayerState {
        return audioPlayerImpl.stateControl()
    }

    override fun updateTimer(): String {
        return audioPlayerImpl.getCurrentTime()
    }

    override fun destroyPlayer() {
        audioPlayerImpl.destroyActivity()
    }

    override fun stopPlayer() {
        audioPlayerImpl.pauseActivity()
    }

}