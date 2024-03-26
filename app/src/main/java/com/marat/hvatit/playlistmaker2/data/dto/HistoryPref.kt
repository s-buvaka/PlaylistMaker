package com.marat.hvatit.playlistmaker2.data.dto

import com.marat.hvatit.playlistmaker2.domain.models.Track

interface HistoryPref {

    fun getItemsFromCache(): List<Track>

    fun saveItemsToCache(newItems: List<Track>)

}