package com.marat.hvatit.playlistmaker2.domain.api.interactors

import com.marat.hvatit.playlistmaker2.domain.models.Track

interface TrackInteractor {

    fun searchTrack(expression: String, consumer: TrackConsumer)

    interface TrackConsumer {
        fun consume(foundTrack: List<Track>?, errorMessage: String?)
    }

}
