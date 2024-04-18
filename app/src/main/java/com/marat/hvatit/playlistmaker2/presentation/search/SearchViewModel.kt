package com.marat.hvatit.playlistmaker2.presentation.search

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.marat.hvatit.playlistmaker2.R
import com.marat.hvatit.playlistmaker2.domain.api.TrackInteractor
import com.marat.hvatit.playlistmaker2.domain.models.Track

class SearchViewModel(
    private val interactor: TrackInteractor
) : ViewModel() {


    private var loadingObserver: ((Boolean) -> Unit)? = null
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

    fun removeLoadingObserver() {
        this.loadingObserver = null
    }

    fun search(query: String) {
        searchState = SearchState.Download
        /*activityState(searchActivityState)*/
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

    companion object {
        fun getViewModelFactory(interactor: TrackInteractor): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    SearchViewModel(
                        interactor
                    )
                }
            }
    }
}
