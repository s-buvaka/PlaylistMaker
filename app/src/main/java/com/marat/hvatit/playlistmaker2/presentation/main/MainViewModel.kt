package com.marat.hvatit.playlistmaker2.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.marat.hvatit.playlistmaker2.domain.api.MainInteractor

class MainViewModel(private val interactor: MainInteractor) : ViewModel() {


    fun isDarkMode(): Boolean {
        return interactor.getPrefTheme()
    }
    companion object {
        fun getViewModelFactory(interactor: MainInteractor): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    MainViewModel(interactor)
                }
            }
    }

}