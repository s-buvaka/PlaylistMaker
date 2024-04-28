package com.marat.hvatit.playlistmaker2.presentation.audioplayer


/*enum class MediaPlayerState {
    STATE_DEFAULT, STATE_PREPARED, STATE_PLAYING, STATE_PAUSED
}*/
sealed interface MediaPlayerState{

    object Default : MediaPlayerState

    object Prepared : MediaPlayerState

    data class Playing(val currentTime: String) : MediaPlayerState

    object Paused : MediaPlayerState

}