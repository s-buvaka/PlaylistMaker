package com.marat.hvatit.playlistmaker2.domain.impl

import com.marat.hvatit.playlistmaker2.domain.api.TrackInteractor
import com.marat.hvatit.playlistmaker2.domain.api.TrackRepository
import java.util.concurrent.Executors

class TrackInteractorImpl(private val repository : TrackRepository):TrackInteractor {

    private val executor = Executors.newCachedThreadPool()
    override fun searchTrack(expression: String, consumer: TrackInteractor.TrackConsumer) {
        executor.execute {
            consumer.consume(repository.searchTrack(expression))
        }
    }
}