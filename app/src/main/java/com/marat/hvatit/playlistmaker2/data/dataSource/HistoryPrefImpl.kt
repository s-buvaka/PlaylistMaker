package com.marat.hvatit.playlistmaker2.data.dataSource

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.marat.hvatit.playlistmaker2.domain.models.Track


private const val KEY_THEME = "night_mode_enabled"
private const val KEY_TRACKS = "items"

class HistoryPrefImpl(val context: Context,private val sharedPreferences: SharedPreferences , private val gson: Gson) : HistoryPref {

    override fun getItemsFromCache(): List<Track> {
        val json: String? = sharedPreferences.getString(KEY_TRACKS, null)
        return if (json != null) {
            Log.e(
                "saveSongStack",
                "getSaveTracks:${
                    gson.fromJson<List<Track>>(
                        json,
                        object : TypeToken<List<Track>>() {}.type
                    )
                }"
            )
            gson.fromJson<List<Track>>(json, object : TypeToken<List<Track>>() {}.type)
        } else emptyList()
    }

    override fun saveItemsToCache(newItems: List<Track>) {
        val json = gson.toJson(newItems)
        sharedPreferences.edit()
            .putString(KEY_TRACKS, json)
            .apply()
    }

    override fun editDefaultTheme(flag: Boolean) {
        sharedPreferences.edit()
            .putBoolean(KEY_THEME, flag)
            .apply()
    }

    override fun getUserTheme(): Boolean {
        if (sharedPreferences.contains(KEY_THEME)) {
            return sharedPreferences.getBoolean(KEY_THEME, false)
        }
        return false
    }
}