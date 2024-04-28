package com.marat.hvatit.playlistmaker2.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.marat.hvatit.playlistmaker2.domain.api.IntentNavigator
import com.marat.hvatit.playlistmaker2.domain.api.SettingsInteractor


class SettingsViewModel(
    private val interactor: SettingsInteractor,
    private val intentNavigator: IntentNavigator
) : ViewModel() {


    fun isDarkMode(): Boolean {
        return interactor.getPrefTheme()
    }

    fun storeMode(isDark: Boolean) {
        interactor.editPrefTheme(isDark)
    }

    companion object {
        fun getViewModelFactory(
            interactor: SettingsInteractor,
            intentNavigator: IntentNavigator
        ): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    SettingsViewModel(interactor, intentNavigator)
                }
            }
    }

    fun createIntent(action: ActionFilter) {
        intentNavigator.createIntent(action)
    }

}