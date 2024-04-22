package com.marat.hvatit.playlistmaker2.data.dto

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.marat.hvatit.playlistmaker2.domain.models.Track

class HistoryPrefImpl(val context: Context) : HistoryPref {

    private val sharedPreferences = context.getSharedPreferences("cart", Context.MODE_PRIVATE)
    private val gson: Gson = Gson()
    override fun getItemsFromCache(): List<Track> {
        val json: String? = sharedPreferences.getString("items", null)
        return if (json != null) {
            Log.e("saveSongStack", "getSaveTracks:${gson.fromJson<List<Track>>(json, object : TypeToken<List<Track>>() {}.type)}")
            gson.fromJson<List<Track>>(json, object : TypeToken<List<Track>>() {}.type)
        } else emptyList()
    }

    override fun saveItemsToCache(newItems: List<Track>) {
        val json = gson.toJson(newItems)
        sharedPreferences.edit()
            .putString("items", json)
            .apply()
    }
}