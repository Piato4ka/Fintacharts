package com.fintacharts

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.fintacharts.ctypto.presentation.CoinScreenRoot
import com.fintacharts.ctypto.presentation.CoinViewModel
import com.fintacharts.ui.theme.FintachartsTheme
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FintachartsTheme {
                val viewModel = koinViewModel<CoinViewModel>()
                CoinScreenRoot(viewModel)
            }
        }
    }
}

