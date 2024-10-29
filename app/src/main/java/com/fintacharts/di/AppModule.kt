package com.fintacharts.di

import com.fintacharts.core.data.local.SharedPrefsRepository
import com.fintacharts.core.data.networking.HttpClientFactory
import com.fintacharts.ctypto.data.networking.RemoteAuthDataSource
import com.fintacharts.ctypto.data.networking.RemoteCoinDataSource
import com.fintacharts.ctypto.domain.AuthDataSource
import com.fintacharts.ctypto.domain.CoinDataSource
import com.fintacharts.ctypto.presentation.CoinViewModel
import io.ktor.client.engine.cio.CIO
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {
    single { SharedPrefsRepository(get()) }
    singleOf(::RemoteCoinDataSource).bind<CoinDataSource>()
    singleOf(::RemoteAuthDataSource).bind<AuthDataSource>()
    single { HttpClientFactory.create(CIO.create(), get(), get()) }
    viewModelOf(::CoinViewModel)

}