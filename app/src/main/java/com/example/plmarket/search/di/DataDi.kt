package com.example.plmarket.search.di

import com.example.plmarket.search.data.NetworkClient
import com.example.plmarket.search.data.dto.TrackRepositoryImpl
import com.example.plmarket.search.data.network.RetrofitNetworkClient
import com.example.plmarket.search.di.impl.SharedPreferensecHistoryImpl
import com.example.plmarket.search.domain.SharedPreferensecHistory
import com.example.plmarket.search.domain.TrackRepository
import org.koin.dsl.module

val dataSearchModule = module {
    single<TrackRepository> {
        TrackRepositoryImpl(networkClient = get())
    }

    single<NetworkClient> {
        RetrofitNetworkClient()
    }

    single<SharedPreferensecHistory> {
        SharedPreferensecHistoryImpl(context = get())
    }
}