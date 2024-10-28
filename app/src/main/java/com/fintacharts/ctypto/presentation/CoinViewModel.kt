package com.fintacharts.ctypto.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fintacharts.core.domain.util.onError
import com.fintacharts.core.domain.util.onSuccess
import com.fintacharts.ctypto.domain.CoinDataSource
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class CoinViewModel(
    private val coinDataSource: CoinDataSource,
) : ViewModel() {

    private val _uiState = MutableStateFlow(CoinScreenUiState())
    val state = _uiState.asStateFlow()

    private val _events = Channel<CoinListEvent>()
    val events = _events.receiveAsFlow()


    fun subscribeToCoinUpdate() {
        viewModelScope.launch {
            coinDataSource.startWebSocket().onSuccess { flow ->
                flow.collect { webSocketDto ->
                    if (webSocketDto.bid != null)
                        _uiState.value =
                            _uiState.value.copy(price = webSocketDto.bid.price.toDisplayableNumber())
                }


            }.onError { error ->
                _events.send(CoinListEvent.Error(error))
            }
        }
    }


    fun getCoinHistory() {
        _uiState.value = _uiState.value.copy(isLoading = true)
        viewModelScope.launch {
            coinDataSource.getCoin()
                .onSuccess { responseCoin ->
                    val mappedPriceList = responseCoin.data.map { it.o }
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        listData = mappedPriceList,
                    )

                }.onError { error ->
                    _uiState.value = _uiState.value.copy(isLoading = false)
                    _events.send(CoinListEvent.Error(error))
                }
        }
    }


}