package com.fintacharts.ctypto.presentation

import java.text.NumberFormat
import java.util.Locale


data class CoinScreenUiState(
    val isLoading: Boolean = false,
    val pair: String = hardcodedPair,
    val price: String = "",
    val formatedDate: String = hardcodedDate,
    val listData: List<Double> = listOf(0.0, 0.0, 0.0, 0.0),
    val listDate: List<String> = listOf("", ""),
)


fun Double.toDisplayableNumber(fraction: Int = 2): String {
    val formatter = NumberFormat.getNumberInstance(Locale.getDefault()).apply {
        maximumFractionDigits = fraction
        maximumFractionDigits = fraction
    }
    return formatter.format(this)

}


val hardcodedPair = "BTC/USD"
val hardcodedDate = "Oct 13, 16:20"
