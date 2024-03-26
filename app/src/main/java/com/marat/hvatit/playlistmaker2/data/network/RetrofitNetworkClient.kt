package com.marat.hvatit.playlistmaker2.data.network

import com.marat.hvatit.playlistmaker2.data.NetworkClient
import com.marat.hvatit.playlistmaker2.data.dto.Response
import com.marat.hvatit.playlistmaker2.data.dto.TrackSearchRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient : NetworkClient {

    private val appleBaseUrl = "https://itunes.apple.com"

    private val retrofit =
        Retrofit.Builder()
            .baseUrl(appleBaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    private val appleService = retrofit.create(AppleMusicApiService::class.java)
    override fun doRequest(dto: Any): Response {
        if (dto is TrackSearchRequest) {
            val resp = appleService.search(dto.expression).execute()

            val body = resp.body() ?: Response()
            return body.apply { resultCode = resp.code() }
        } else {
            return Response().apply { resultCode = 400 }
        }
    }
}