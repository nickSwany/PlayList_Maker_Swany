package com.example.pl_market.domain.impl

import com.example.pl_market.domain.api.TrackInteractor
import com.example.pl_market.domain.api.TrackRepository
import java.util.concurrent.Executors

class TracksInteractorImpl(private val repository: TrackRepository): TrackInteractor {
    private val executor = Executors.newCachedThreadPool()

    override fun searchTrack(expression: String, consumer: TrackInteractor.TrackConsumer) {
        executor.execute {
            consumer.consume(repository.searchTrack(expression))
        }
    }
}