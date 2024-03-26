package com.marat.hvatit.playlistmaker2.domain.api

import com.marat.hvatit.playlistmaker2.domain.models.Track

interface TrackRepository {
    fun searchTrack(expression : String) : List<Track>
}