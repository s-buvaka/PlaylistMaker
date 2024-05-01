package com.marat.hvatit.playlistmaker2.domain.api.interactors.impl

import com.marat.hvatit.playlistmaker2.domain.api.interactors.TrackInteractor
import com.marat.hvatit.playlistmaker2.domain.api.repository.TrackRepository
import com.marat.hvatit.playlistmaker2.presentation.utils.Resource
import java.util.concurrent.Executors

class TrackInteractorImpl(private val repository: TrackRepository) : TrackInteractor {

    private val executor = Executors.newCachedThreadPool()
    override fun searchTrack(expression: String, consumer: TrackInteractor.TrackConsumer) {
        executor.execute {
            when (val resource = repository.searchTrack(expression)) {
                is Resource.Success -> {
                    consumer.consume(resource.data, null)
                }

                is Resource.Error -> {
                    consumer.consume(null, resource.message)
                }
            }
        }
    }
}
