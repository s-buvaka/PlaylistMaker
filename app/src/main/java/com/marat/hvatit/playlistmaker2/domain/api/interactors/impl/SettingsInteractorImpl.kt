package com.marat.hvatit.playlistmaker2.domain.api.interactors.impl

import com.marat.hvatit.playlistmaker2.data.dataSource.HistoryStorage
import com.marat.hvatit.playlistmaker2.domain.api.interactors.SettingsInteractor

class SettingsInteractorImpl(private val historyStorage: HistoryStorage) : SettingsInteractor {
    override fun getPrefTheme(): Boolean {
        return historyStorage.getUserTheme()
    }

    override fun editPrefTheme(value: Boolean) {
        historyStorage.editDefaultTheme(value)
    }


}
