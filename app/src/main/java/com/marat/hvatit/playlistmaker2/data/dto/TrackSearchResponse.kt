package com.marat.hvatit.playlistmaker2.data.dto

// тоже лишний data
data class TrackSearchResponse(
    val searchType: String,
    val expression: String,
    val results: List<TrackDto>
) : Response()
