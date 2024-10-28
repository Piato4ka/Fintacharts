package com.fintacharts.ctypto.presentation

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.fintacharts.core.presentation.util.toString
import com.fintacharts.ctypto.presentation.components.Chart
import com.fintacharts.ui.theme.FintachartsTheme

@Composable
fun CoinScreenRoot(
    viewModel: CoinViewModel,
) {
    val uiState by viewModel.state.collectAsState()

    val context = LocalContext.current
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
    LaunchedEffect(lifecycleOwner.lifecycle) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.events.collect { event ->
                when (event) {
                    is CoinListEvent.Error -> {
                        Toast.makeText(
                            context,
                            event.error.toString(context),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }


    CoinScreen(
        pair = uiState.pair,
        price = uiState.price,
        formatedDate = uiState.formatedDate,
        priceList = uiState.listData,
        isLoading = uiState.isLoading,

        onSubscribeClicked = {
            viewModel.subscribeToCoinUpdate()
            viewModel.getCoinHistory()
        }
    )

}


@Composable
fun CoinScreen(
    pair: String,
    price: String,
    formatedDate: String,
    isLoading: Boolean = false,
    priceList: List<Double>,
    onSubscribeClicked: () -> Unit = {},
) {
    val defaultSpace = 16.dp
    val textMeasurer = rememberTextMeasurer()
    val maxPriceText = priceList.maxOrNull()?.toString() ?: ""

    val textHeightDp = with(LocalDensity.current) {
        val textLayoutResult = textMeasurer.measure(
            text = maxPriceText,
            style = MaterialTheme.typography.bodyMedium
        )
        textLayoutResult.size.height.toDp() // Convert height from px to dp
    }
    val horizontalLineCount = 10

    Surface(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = pair,
                onValueChange = {}
            )
            Spacer(modifier = Modifier.size(defaultSpace))
            Button(
                modifier = Modifier,
                onClick = onSubscribeClicked
            ) {

                Text(
                    text = "Subscribe",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            Spacer(modifier = Modifier.size(defaultSpace))
            Text(
                text = "Market data:",
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.size(defaultSpace))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = "Symbol: \n$pair",
                    style = MaterialTheme.typography.bodyLarge
                )
                if (isLoading)
                    CircularProgressIndicator()
                else
                    Text(
                        text = "Price: \n$price",
                        style = MaterialTheme.typography.bodyLarge
                    )
                Text(
                    text = "Time: \n$formatedDate",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            Spacer(modifier = Modifier.size(defaultSpace * 2))

            Row(
                modifier = Modifier
                    .fillMaxHeight(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    val step = (priceList.max() - priceList.min()) / horizontalLineCount
                    for (i in 0..horizontalLineCount) {
                        val priceStep = priceList.max() - step * i
                        Text(
                            text = priceStep.toDisplayableNumber(5),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
                Spacer(modifier = Modifier.size(4.dp))
                Chart(
                    modifier = Modifier.padding(top = textHeightDp / 2, bottom = textHeightDp / 2),
                    horizontalLineCount = horizontalLineCount,
                    priceList = priceList
                )
            }

        }
    }


}


@Preview
@Composable
private fun CoinScreenPreview() {
    FintachartsTheme {
        CoinScreen(
            pair = "BTC/USD",
            price = "6000",
            priceList = listOf(),
            formatedDate = "Oct 13, 16:20",
        )
    }
}