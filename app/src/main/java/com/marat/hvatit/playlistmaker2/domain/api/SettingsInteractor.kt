package com.marat.hvatit.playlistmaker2.domain.api

interface SettingsInteractor {

    fun getPrefTheme() : Boolean

    fun editPrefTheme(value : Boolean)

}