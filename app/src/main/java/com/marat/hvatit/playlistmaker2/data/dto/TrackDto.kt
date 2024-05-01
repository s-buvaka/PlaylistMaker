package com.marat.hvatit.playlistmaker2.data.dto

import com.google.gson.annotations.SerializedName

// хорошим тоном делать все переменные с аннотацией  @SerializedName(...)
// ты не зависишь от названия переменных на беке и можешь менять их нейминг раздельно.
data class TrackDto( // обычно DTO модельки не надо делать data
    val trackId: String,
    val trackName: String,
    val artistName: String,
    @SerializedName("trackTimeMillis")
    val trackTimeMills: String,
    val artworkUrl100: String,
    @SerializedName("country")
    val country: String,
    @SerializedName("primaryGenreName")
    val genre: String,
    @SerializedName("releaseDate")
    val year: String,
    @SerializedName("collectionName")
    val album: String,
    @SerializedName("previewUrl")
    val priviewUrl: String
)
