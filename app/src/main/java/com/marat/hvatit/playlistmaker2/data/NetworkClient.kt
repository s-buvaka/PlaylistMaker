package com.marat.hvatit.playlistmaker2.data

import com.marat.hvatit.playlistmaker2.data.dto.Response
import com.marat.hvatit.playlistmaker2.data.dto.TrackSearchRequest
import com.marat.hvatit.playlistmaker2.data.dto.TrackSearchResponse

interface NetworkClient {

    //  опционально
    fun search(trackSearchRequest: TrackSearchRequest): retrofit2.Response<TrackSearchResponse>
    fun doRequest(dto: Any): Response

}
