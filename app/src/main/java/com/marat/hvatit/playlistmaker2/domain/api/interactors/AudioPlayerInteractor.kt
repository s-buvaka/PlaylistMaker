package com.marat.hvatit.playlistmaker2.domain.api.interactors

import com.marat.hvatit.playlistmaker2.presentation.audioplayer.MediaPlayerState

// так же не интереракотр.
// он вообще не нужен ибо просто копирует AudioPlayerRepository
interface AudioPlayerInteractor {
    fun playbackControl(): MediaPlayerState

    fun updateTimer(): String

    fun destroyPlayer()

    fun stopPlayer()
}
