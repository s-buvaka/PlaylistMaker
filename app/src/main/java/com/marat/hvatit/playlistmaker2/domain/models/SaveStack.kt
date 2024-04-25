package com.marat.hvatit.playlistmaker2.domain.models

import com.marat.hvatit.playlistmaker2.data.dataSource.HistoryPref
import java.util.Stack

class SaveStack<T>( private val maxSize: Int,private val historyPref : HistoryPref) : Stack<Track>() {
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
        return historyPref.getItemsFromCache()
    }

    private fun saveItemsToCache(newItems: List<Track>) {
        historyPref.saveItemsToCache(newItems)
    }

}