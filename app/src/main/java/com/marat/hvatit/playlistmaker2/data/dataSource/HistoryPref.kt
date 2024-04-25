package com.marat.hvatit.playlistmaker2.data.dataSource

import com.marat.hvatit.playlistmaker2.domain.models.Track

interface HistoryPref {

    fun getItemsFromCache(): List<Track>

    fun saveItemsToCache(newItems: List<Track>)

    fun editDefaultTheme(flag: Boolean)

    fun getUserTheme() : Boolean

}