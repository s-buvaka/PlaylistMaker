package com.marat.hvatit.playlistmaker2.domain.models

import android.content.Context
import com.marat.hvatit.playlistmaker2.data.dto.HistoryPrefImpl
import java.util.Stack

class SaveStack<T>(context: Context, private val maxSize: Int) : Stack<Track>() {

    private val historyPref = HistoryPrefImpl(context)
    fun pushElement(item: Track){
        if(size>=maxSize){
            this.removeAt(this.size - 1)
        }
        this.add(0,item)
        saveItemsToCache(this)
    }


    fun searchId(item: Track): Boolean {
        for (i in this) {
            if (i.trackId == item.trackId) {
                return true
            }
        }
        return false
    }

    fun onDestroyStack() {
        this.also {
            saveItemsToCache(it)
        }
    }

    fun getItemsFromCache(): List<Track>? {
        //Log.e("saveSongStack", "getSaveTracks:${historyPref.getItemsFromCache()}")
        return historyPref.getItemsFromCache()
    }

    private fun saveItemsToCache(newItems: List<Track>) {
        historyPref.saveItemsToCache(newItems)
    }

    fun interface SaveStackItemsListener {
        //Не успеваю реализовать, форсмажор
        fun onItemsChanged(items: List<Track>)
    }

}