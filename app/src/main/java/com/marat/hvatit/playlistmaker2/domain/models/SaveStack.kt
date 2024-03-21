package com.marat.hvatit.playlistmaker2.domain.models

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.marat.hvatit.playlistmaker2.data.dto.HistoryPref
import com.marat.hvatit.playlistmaker2.data.dto.HistoryPrefImpl
import com.marat.hvatit.playlistmaker2.data.network.AppleSong
import java.util.Stack

class SaveStack<T>(context: Context, private val maxSize: Int) : Stack<AppleSong>() {

    private val historyPref = HistoryPrefImpl(context)
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
        return historyPref.getItemsFromCache()
    }

    private fun saveItemsToCache(newItems: List<AppleSong>) {
        historyPref.saveItemsToCache(newItems)
    }

    fun interface SaveStackItemsListener {
        //Не успеваю реализовать, форсмажор
        fun onItemsChanged(items: List<AppleSong>)
    }

}