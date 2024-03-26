package com.marat.hvatit.playlistmaker2.data.network

import com.marat.hvatit.playlistmaker2.domain.models.Track

interface TrackRepository {
    fun searchMovies(expression: String): List<Track>

}