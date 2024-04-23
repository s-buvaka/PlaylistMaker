package com.marat.hvatit.playlistmaker2.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.marat.hvatit.playlistmaker2.data.NetworkClient
import com.marat.hvatit.playlistmaker2.data.dto.Response
import com.marat.hvatit.playlistmaker2.data.dto.TrackSearchRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient(private val context: Context) : NetworkClient {

    private val appleBaseUrl = "https://itunes.apple.com"

    private val retrofit =
        Retrofit.Builder()
            .baseUrl(appleBaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    private val appleService = retrofit.create(AppleMusicApiService::class.java)
    override fun doRequest(dto: Any): Response {
        if (dto is TrackSearchRequest) {
            if (isConnected() == false) {
                return Response().apply { resultCode = -1 }
            }
            val resp = appleService.search(dto.expression).execute()

            val body = resp.body() ?: Response()
            return body.apply { resultCode = resp.code() }
        } else {
            return Response().apply { resultCode = 400 }
        }
    }

    private fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager

        val capabilities = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        } else {
            //если версия меньше 23
            return false
        }
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            }
        }
        return false
    }
}