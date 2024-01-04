package com.example.plmarket.search.domain.Impl

import com.example.plmarket.Resource
import com.example.plmarket.search.domain.TrackInteractor
import com.example.plmarket.search.domain.TrackRepository
import java.util.concurrent.Executors

class TrackInteractorImpl(private val repository: TrackRepository): TrackInteractor {
    private val executor = Executors.newCachedThreadPool()
    override fun searchTrack(expression: String, consumer: TrackInteractor.TrackConsumer) {
        executor.execute {
            when ( val resource = repository.searchTrack(expression)) {
                is Resource.Success -> consumer.consume(resource.data, null)
                is Resource.Error -> consumer.consume(null, resource.message)
            }
        }
    }
}

