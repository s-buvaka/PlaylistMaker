package com.marat.hvatit.playlistmaker2.presentation.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

    private var searchState: SearchState = SearchState.StartState

    private var loadingLiveData = MutableLiveData(searchState)
    init {
        saveSongStack.addAll(getSaveTracks())

    }

    fun getLoadingLiveData(): LiveData<SearchState> = loadingLiveData

    fun changeState(newState: SearchState) {
        loadingLiveData.postValue(newState)
    }


    fun search(query: String) {
        searchState = SearchState.Download
        interactor.searchTrack(query, object : TrackInteractor.TrackConsumer {
            override fun consume(foundTrack: List<Track>) {
                if (foundTrack.isEmpty()) {
                    loadingLiveData.postValue(SearchState.NothingToShow(R.string.act_search_nothing))
                } else {
                    loadingLiveData.postValue(SearchState.Data(foundTrack))
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

    fun setSaveTracks() {
        this.saveSongStack.onDestroyStack()
    }

    fun isEmptyStack(): Boolean {
        return this.saveSongStack.isEmpty()
    }

    fun clearSaveStack() {
        saveSongStack.clear()
        setSaveTracks()
    }

    fun getSaveTracks(): List<Track> {
        return saveSongStack.getItemsFromCache()?.toList() ?: listOf()
    }

    companion object {
        fun getViewModelFactory(
            interactor: TrackInteractor,
            saveSongStack: SaveStack<Track>
        ): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    SearchViewModel(
                        interactor, saveSongStack
                    )
                }
            }
    }
}
