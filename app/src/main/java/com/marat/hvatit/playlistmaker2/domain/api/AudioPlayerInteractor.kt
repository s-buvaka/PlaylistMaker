package com.marat.hvatit.playlistmaker2.domain.api

import com.marat.hvatit.playlistmaker2.presentation.audioplayer.MediaPlayerState

interface AudioPlayerInteractor {
    fun playbackControl(): MediaPlayerState

    fun updateTimer(): String

    fun destroyPlayer()

    fun stopPlayer()
}