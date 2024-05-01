package com.marat.hvatit.playlistmaker2.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.marat.hvatit.playlistmaker2.data.NetworkClient
import com.marat.hvatit.playlistmaker2.data.dto.Response
import com.marat.hvatit.playlistmaker2.data.dto.TrackSearchRequest
import com.marat.hvatit.playlistmaker2.data.dto.TrackSearchResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient(
    private val context: Context,
//    private val appleBaseUrl: String  - можно провайдить извне
) : NetworkClient {

    // в константу
    // или еще лучше провайдить ее снаружи. В данной случа опционально
    private val appleBaseUrl = "https://itunes.apple.com"

    private val retrofit =
        Retrofit.Builder()
            .baseUrl(appleBaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    private val appleService = retrofit.create(AppleMusicApiService::class.java)

    // можно сделать так, но если вас учили как что-то оборачивать, забей - и оставь как было
    override fun search(trackSearchRequest: TrackSearchRequest): retrofit2.Response<TrackSearchResponse> {
        return appleService.search(trackSearchRequest.expression).execute()
    }

    // поскольку у тебя тут есть вызов конкретного метода appleService.search - то и назвать его нужно  search
    override fun doRequest(dto: Any): Response {
        if (dto is TrackSearchRequest) {
            if (!isConnected()) {
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
