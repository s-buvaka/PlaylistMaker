package com.marat.hvatit.playlistmaker2.data.dto

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.marat.hvatit.playlistmaker2.data.network.AppleSong

class HistoryPrefImpl(val context: Context) : HistoryPref {

    private val sharedPreferences = context.getSharedPreferences("cart", Context.MODE_PRIVATE)
    private val gson: Gson = Gson()
    override fun getItemsFromCache(): List<AppleSong> {
        val json: String? = sharedPreferences.getString("items", null)
        return if (json != null) {
            gson.fromJson<List<AppleSong>>(json, object : TypeToken<List<AppleSong>>() {}.type)
        } else emptyList()
    }

    override fun saveItemsToCache(newItems: List<AppleSong>) {
        val json = gson.toJson(newItems)
        sharedPreferences.edit()
            .putString("items", json)
            .apply()
    }
}