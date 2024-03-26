package com.marat.hvatit.playlistmaker2.data.network

import com.marat.hvatit.playlistmaker2.data.dto.TrackSearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface AppleMusicApiService {
    @GET("/search?entity=song")
    fun search(@Query("term") text: String): Call<TrackSearchResponse>
}