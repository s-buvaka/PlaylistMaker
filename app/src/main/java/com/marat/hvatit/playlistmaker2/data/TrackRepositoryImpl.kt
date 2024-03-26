package com.marat.hvatit.playlistmaker2.data

import com.marat.hvatit.playlistmaker2.domain.models.Track
import com.marat.hvatit.playlistmaker2.data.dto.TrackSearchRequest
import com.marat.hvatit.playlistmaker2.data.dto.TrackSearchResponse
import com.marat.hvatit.playlistmaker2.domain.api.TrackRepository

class TrackRepositoryImpl(private val networkClient: NetworkClient) : TrackRepository {
    override fun searchTrack(expression: String): List<Track> {
        val response = networkClient.doRequest(TrackSearchRequest(expression))
        if (response.resultCode == 200) {
            return (response as TrackSearchResponse).results.map {
                Track(
                    it.trackId,
                    it.trackName,
                    it.artistName,
                    it.trackTimeMills,
                    it.artworkUrl100,
                    it.country,
                    it.genre,
                    it.year ?: "0",
                    it.album,
                    it.priviewUrl ?: "0"
                )
            }
        } else {
            return emptyList()
        }
    }
}