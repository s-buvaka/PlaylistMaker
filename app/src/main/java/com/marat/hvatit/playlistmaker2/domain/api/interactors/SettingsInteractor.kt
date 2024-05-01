package com.marat.hvatit.playlistmaker2.domain.api.interactors

// опционально, но может быть стоит разделить на два отдельных в каждом по одному методу: getPrefTheme() : Boolean, editPrefTheme(value : Boolean)
interface SettingsInteractor {

    fun getPrefTheme() : Boolean

    fun editPrefTheme(value : Boolean)

}
