package com.marat.hvatit.playlistmaker2.presentation.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.marat.hvatit.playlistmaker2.domain.api.interactors.SettingsInteractor


class SettingsViewModel(
    private val interactor: SettingsInteractor,
    private val intentNavigator: IntentNavigator
) : ViewModel() {

    private var settingsThemeState: Boolean = interactor.getPrefTheme()

    private var loadingLiveData = MutableLiveData(settingsThemeState)

    fun getLoadingLiveData(): LiveData<Boolean> = loadingLiveData

    fun storeMode(isDark: Boolean) {
        loadingLiveData.postValue(isDark)
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
