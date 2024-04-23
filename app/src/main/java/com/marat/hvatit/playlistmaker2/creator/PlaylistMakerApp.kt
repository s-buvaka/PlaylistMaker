package com.marat.hvatit.playlistmaker2.creator

import android.app.Application
import android.content.Context

class PlaylistMakerApp: Application() {

    init {
        instance = this
    }

    companion object {
        private var instance: PlaylistMakerApp? = null

        fun applicationContext() : Context {
            return instance!!.applicationContext
        }
    }
}