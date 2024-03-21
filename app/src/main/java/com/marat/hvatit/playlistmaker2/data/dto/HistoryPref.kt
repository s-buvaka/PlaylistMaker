package com.marat.hvatit.playlistmaker2.data.dto

import com.marat.hvatit.playlistmaker2.data.network.AppleSong

interface HistoryPref {

    fun getItemsFromCache(): List<AppleSong>

    fun saveItemsToCache(newItems: List<AppleSong>)

}