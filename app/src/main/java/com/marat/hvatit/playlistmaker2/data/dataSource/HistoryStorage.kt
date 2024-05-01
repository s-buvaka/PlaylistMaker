package com.marat.hvatit.playlistmaker2.data.dataSource

import com.marat.hvatit.playlistmaker2.domain.models.Track

// есть смысл переимновать отвязаться от Pref: HistoryPref -> HistoryStorage
interface HistoryStorage {

    // опционально вместо getItemsFromCache и saveItemsToCache
    var trackList: List<Track>

    // есть смысл переименовать getItemsFromCache -> getTrackList
    fun getTrackList(): List<Track>

    // есть смысл переименовать saveItemsToCache -> saveTrackList
    fun saveTrackList(newItems: List<Track>)

    fun editDefaultTheme(flag: Boolean)

    fun getUserTheme() : Boolean

}
