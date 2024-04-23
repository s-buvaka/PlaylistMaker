package com.marat.hvatit.playlistmaker2.data

import com.marat.hvatit.playlistmaker2.data.dto.TrackSearchRequest
import com.marat.hvatit.playlistmaker2.data.dto.TrackSearchResponse
import com.marat.hvatit.playlistmaker2.domain.api.TrackRepository
import com.marat.hvatit.playlistmaker2.domain.models.Track
import com.marat.hvatit.playlistmaker2.presentation.utils.Resource

class TrackRepositoryImpl(private val networkClient: NetworkClient) : TrackRepository {
    override fun searchTrack(expression: String): Resource<List<Track>> {
        val response = networkClient.doRequest(TrackSearchRequest(expression))
        return when (response.resultCode) {
            -1 -> {
                Resource.Error("ERROR1111!")
            }

            200 -> {
                Resource.Success((response as TrackSearchResponse).results.map {
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
                })
            }

            else -> {
                Resource.Error("Ошибка сервера")
            }
        }
    }
}