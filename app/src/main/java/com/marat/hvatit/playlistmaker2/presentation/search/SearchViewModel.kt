package com.marat.hvatit.playlistmaker2.presentation.search

import android.util.Log
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.marat.hvatit.playlistmaker2.R
import com.marat.hvatit.playlistmaker2.domain.api.TrackInteractor
import com.marat.hvatit.playlistmaker2.domain.models.SaveStack
import com.marat.hvatit.playlistmaker2.domain.models.Track

class SearchViewModel(
    private val interactor: TrackInteractor, private var saveSongStack: SaveStack<Track>
) : ViewModel() {
    init {
        saveSongStack.addAll(getSaveTracks())
        Log.e("SaveTracks","Init:${saveSongStack.getItemsFromCache()?.toList() ?: listOf()}")
    }

    private var searchStateObservers: ((SearchState) -> Unit)? = null

    var searchState: SearchState = SearchState.StartState
        private set(value){
            field = value
            searchStateObservers?.invoke(value)
        }

    fun changeState(newState:SearchState){
        searchState = newState
    }

    fun addSearchObserver(searchObserver: ((SearchState) -> Unit)) {
        this.searchStateObservers = searchObserver
    }

    fun removeSearchStateObservers() {
        this.searchStateObservers = null
    }

    fun search(query: String) {
        searchState = SearchState.Download
        interactor.searchTrack(query, object : TrackInteractor.TrackConsumer {
            override fun consume(foundTrack: List<Track>) {
                if (foundTrack.isEmpty()){
                    @StringRes
                    searchState = SearchState.NothingToShow(R.string.act_search_nothing)
                }
                else{
                    searchState = SearchState.Data(foundTrack)
                }
            }
        })
    }
    fun addSaveSongs(item: Track) {
        if (saveSongStack.searchId(item)) {
            this.saveSongStack.remove(item)
        }
        this.saveSongStack.pushElement(item)
    }

    fun setSaveTracks(){
        Log.e("SaveTracks","${this.saveSongStack.getItemsFromCache()}")
        this.saveSongStack.onDestroyStack()
    }

    fun isEmptyStack():Boolean{
        return this.saveSongStack.isEmpty()
    }

    fun clearSaveStack(){
        saveSongStack.clear()
        setSaveTracks()
    }

    fun getSaveTracks(): List<Track> {
        //saveSongStack.addAll(saveSongStack.getItemsFromCache()?.toList() ?: listOf())
        Log.e("saveSongStack", "getSaveTracks:${saveSongStack.getItemsFromCache()?.toList()?: listOf()}")
        return saveSongStack.getItemsFromCache()?.toList() ?: listOf()
    }

    companion object {
        fun getViewModelFactory(interactor: TrackInteractor,saveSongStack: SaveStack<Track>): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    SearchViewModel(
                        interactor, saveSongStack
                    )
                }
            }
    }
}
