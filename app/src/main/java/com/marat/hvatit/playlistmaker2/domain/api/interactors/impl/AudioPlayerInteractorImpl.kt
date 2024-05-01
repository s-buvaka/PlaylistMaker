package com.marat.hvatit.playlistmaker2.domain.api.interactors.impl


import com.marat.hvatit.playlistmaker2.domain.api.interactors.AudioPlayerInteractor
import com.marat.hvatit.playlistmaker2.domain.api.repository.AudioPlayerRepository
import com.marat.hvatit.playlistmaker2.presentation.audioplayer.MediaPlayerState

// он вообще не нужен ибо просто копирует AudioPlayerRepository
class AudioPlayerInteractorImpl(private val audioPlayerImpl: AudioPlayerRepository) : // audioPlayerImpl -> переименовать в что-то типа audioPlayerController
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
