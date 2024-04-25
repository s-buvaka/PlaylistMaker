package com.marat.hvatit.playlistmaker2.domain.api

import com.marat.hvatit.playlistmaker2.presentation.settings.ActionFilter

interface IntentNavigator {
    fun createIntent(action : ActionFilter)

}