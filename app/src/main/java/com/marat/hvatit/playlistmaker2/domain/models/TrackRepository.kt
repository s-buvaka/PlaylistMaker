package com.marat.hvatit.playlistmaker2.domain.models

import com.bumptech.glide.Glide.init
import com.marat.hvatit.playlistmaker2.data.dataSource.HistoryStorage
import java.util.Stack
import kotlin.random.Random

// вот это как раз репозиторий.
// ему лучше работать с внутренним стеком (если это надо) и он работает со стораджем
// его надо отправить в data
class TrackRepository(
    private val maxSize: Int,
    private val historyStorage: HistoryStorage
) {

    // внутреннее состояние треков
    private val _tracks: Stack<Track> = Stack()

    // отвечает за текущее состояние
    val tracks: List<Track>
        get() = _tracks

    init {
        _tracks.addAll(historyStorage.trackList)
    }

    fun pushElement(item: Track) {
        if (_tracks.size >= maxSize) {
            _tracks.removeAt(_tracks.size - 1)
        }
        _tracks.add(0, item)
        saveItemsToCache(_tracks)
    }

    fun remove(item: Track) {
        _tracks.remove(item)
    }

    fun clear() {
        _tracks.clear()
    }

    fun searchId(item: Track): Boolean {
        for (i in _tracks) {
            if (i.trackId == item.trackId) {
                return true
            }
        }
        return false
    }

    fun onDestroyStack() {
        saveItemsToCache(_tracks)
    }

    private fun saveItemsToCache(newItems: List<Track>) {
        historyStorage.trackList = newItems
    }
}
