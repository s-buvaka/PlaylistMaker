package com.marat.hvatit.playlistmaker2.domain.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName

// в доменных сущностнях не надо ничего про сериализацию
// удалить все @SerializedName
data class Track(
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
