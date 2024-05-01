package com.marat.hvatit.playlistmaker2.presentation.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.marat.hvatit.playlistmaker2.R
import com.marat.hvatit.playlistmaker2.domain.api.interactors.TrackInteractor
import com.marat.hvatit.playlistmaker2.domain.models.TrackRepository
import com.marat.hvatit.playlistmaker2.domain.models.Track

class SearchViewModel(
    private val interactor: TrackInteractor, private var trackRepository: TrackRepository
) : ViewModel() {

    private var searchState: SearchState = SearchState.StartState(trackRepository.tracks)
    private var loadingLiveData = MutableLiveData(searchState)

    init {
        Log.e("newSaveStack", "${trackRepository.tracks}")
    }

    fun getLoadingLiveData(): LiveData<SearchState> = loadingLiveData

    fun changeState(newState: SearchState) {
        loadingLiveData.postValue(newState)
    }


    fun search(query: String) {
        searchState = SearchState.Download
        interactor.searchTrack(query, object : TrackInteractor.TrackConsumer {
            override fun consume(foundTrack: List<Track>?, errorMessage: String?) {
                if (foundTrack != null) {
                    if (foundTrack.isEmpty()) {
                        loadingLiveData.postValue(SearchState.NothingToShow(R.string.act_search_nothing))
                    } else {
                        loadingLiveData.postValue(SearchState.Data(foundTrack))
                    }
                } else {
                    loadingLiveData.postValue(SearchState.Disconnected(R.string.act_search_disconnect))
                }
            }
        })
    }

    fun addSaveSongs(item: Track) {
        if (trackRepository.searchId(item)) {
            trackRepository.remove(item)
        }
        trackRepository.pushElement(item)

        if (loadingLiveData.value is SearchState.StartState) {
            setSavedTracks()
        }
    }

    fun saveTracksToCache() {
        this.trackRepository.onDestroyStack()
    }

    fun clearSaveStack() {
        trackRepository.clear()
        saveTracksToCache()
    }

    fun setSavedTracks() {
        if (trackRepository.tracks.isEmpty()) {
            loadingLiveData.postValue(SearchState.ClearState)
        } else {
            loadingLiveData.postValue(
                SearchState.StartState(
                    trackRepository.tracks
                )
            )
        }
        //return saveSongStack.getItemsFromCache()?.toList() ?: listOf()
    }

    companion object {
        fun getViewModelFactory(
            interactor: TrackInteractor,
            saveSongStack: TrackRepository
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
