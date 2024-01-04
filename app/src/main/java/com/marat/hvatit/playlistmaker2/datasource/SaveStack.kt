package com.marat.hvatit.playlistmaker2.datasource

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.marat.hvatit.playlistmaker2.service.AppleSong
import java.util.Stack

class SaveStack<T>(private val context: Context, private val maxSize: Int) : Stack<AppleSong>() {

    private val sharedPreferences = context.getSharedPreferences("cart", Context.MODE_PRIVATE)
    private val gson: Gson = Gson()
    var stackItemsListener: SaveStackItemsListener? = null
    fun pushElement(item: AppleSong){
        if(size>=maxSize){
            this.removeAt(this.size - 1)
        }
        this.add(0,item)
        saveItemsToCache(this)
    }


    fun searchId(item: AppleSong): Boolean {
        for (i in this) {
            if (i.trackId == item.trackId) {
                return true
            }
        }
        return false
    }

    fun onStop() {
        this?.also {
            saveItemsToCache(it)
        }
    }

    fun getItemsFromCache(): List<AppleSong>? {
        val json: String? = sharedPreferences.getString("items", null)
        return if (json != null) {
            gson.fromJson<List<AppleSong>>(json, object : TypeToken<List<AppleSong>>() {}.type)
        } else {
            null
        }
    }

    private fun saveItemsToCache(newItems: SaveStack<T>) {
        val json = gson.toJson(newItems)
        sharedPreferences.edit()
            .putString("items", json)
            .apply()
    }

    fun interface SaveStackItemsListener {
        //Не успеваю реализовать, форсмажор
        fun onItemsChanged(items: List<AppleSong>)
    }

}