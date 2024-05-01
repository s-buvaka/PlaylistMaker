package com.marat.hvatit.playlistmaker2.domain.api.repository

import com.marat.hvatit.playlistmaker2.presentation.audioplayer.MediaPlayerState

// переименовать и оптравить в презентейшн
interface AudioPlayerRepository {

    fun stateControl(): MediaPlayerState

    fun getCurrentTime(): String

    fun destroyActivity()

    fun pauseActivity()

}
