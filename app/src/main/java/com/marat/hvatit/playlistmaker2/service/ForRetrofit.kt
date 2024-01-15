package com.marat.hvatit.playlistmaker2.service

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


class AppleSongResponce(val resultcount: Int, val results: ArrayList<AppleSong>) {
    override fun toString(): String {
        return "AppleSongResponce(resultcount=$resultcount, results=$results)"
    }
}

data class AppleSong(
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
    val album: String
) {
    override fun toString(): String {
        return "AppleSong(trackId='$trackId', trackName='$trackName', artistName='$artistName', trackTimeMills='$trackTimeMills', artworkUrl100='$artworkUrl100', country='$country', genre='$genre', year='$year', album='$album')"
    }

}

interface AppleMusicAPI {
    @GET("/search?entity=song")
    fun search(@Query("term") text: String): Call<AppleSongResponce>
}