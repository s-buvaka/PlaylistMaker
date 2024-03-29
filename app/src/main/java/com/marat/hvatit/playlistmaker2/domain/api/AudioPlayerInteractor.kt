package com.marat.hvatit.playlistmaker2.domain.api

import com.marat.hvatit.playlistmaker2.presentation.audioplayer.MediaPlayerState

interface AudioPlayerInteractor {

    fun stateControl(): MediaPlayerState

    fun getCurrentTime(): String

    fun destroyActivity()

    fun pauseActivity()

}