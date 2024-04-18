package com.marat.hvatit.playlistmaker2.presentation.search

import com.marat.hvatit.playlistmaker2.domain.models.Track


sealed interface SearchState {
    //DISCONNECTED, NOTHINGTOSHOW, ALLFINE, STARTSTATE, CLEARSTATE, DOWNLOAD
    data class Data(val foundTrack: List<Track>) : SearchState
    class Disconnected(val message: Int) : SearchState
    class NothingToShow(val message: Int) : SearchState

    object StartState : SearchState

    object ClearState : SearchState

    object AllFine : SearchState

    object Download : SearchState
}