package com.marat.hvatit.playlistmaker2.domain.impl

import com.marat.hvatit.playlistmaker2.data.dataSource.HistoryPref
import com.marat.hvatit.playlistmaker2.domain.api.MainInteractor

class MainInteractorImpl(private val historyPref: HistoryPref) : MainInteractor {
    override fun getPrefTheme(): Boolean {
        return historyPref.getUserTheme()
    }
}