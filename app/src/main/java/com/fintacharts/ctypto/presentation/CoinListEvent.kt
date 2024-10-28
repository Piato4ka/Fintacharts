package com.fintacharts.ctypto.presentation

import com.fintacharts.core.domain.util.NetworkError


sealed interface CoinListEvent {
    data class Error(val error: NetworkError) : CoinListEvent
}