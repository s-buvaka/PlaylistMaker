package com.marat.hvatit.playlistmaker2.domain.impl

import com.marat.hvatit.playlistmaker2.data.dataSource.HistoryPref
import com.marat.hvatit.playlistmaker2.domain.api.SettingsInteractor

class SettingsInteractorImpl(private val historyPref: HistoryPref) : SettingsInteractor {
    override fun getPrefTheme(): Boolean {
        return historyPref.getUserTheme()
    }

    override fun editPrefTheme(value: Boolean) {
        historyPref.editDefaultTheme(value)
    }


}