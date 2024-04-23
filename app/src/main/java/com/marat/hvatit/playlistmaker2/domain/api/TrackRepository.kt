package com.marat.hvatit.playlistmaker2.domain.api

import com.marat.hvatit.playlistmaker2.domain.models.Track
import com.marat.hvatit.playlistmaker2.presentation.utils.Resource

interface TrackRepository {
    fun searchTrack(expression : String) : Resource<List<Track>>
}