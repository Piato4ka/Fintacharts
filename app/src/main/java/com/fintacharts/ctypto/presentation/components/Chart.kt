package com.fintacharts.ctypto.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.fintacharts.ui.theme.FintachartsTheme

@Composable
fun Chart(
    modifier: Modifier = Modifier,
    horizontalLineCount: Int = 10,
    priceList: List<Double>
) {
    val zipList: List<Pair<Double, Double>> = priceList.zipWithNext()


    Row(modifier = modifier) {
        val max = priceList.max()
        val min = priceList.min()


        for (pair in zipList) {

            val fromValuePercentage = getValuePercentageForRange(pair.first, max, min)
            val toValuePercentage = getValuePercentageForRange(pair.second, max, min)

            Canvas(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f),
                onDraw = {
                    val fromPoint = Offset(
                        x = 0f,
                        y = size.height.times(1 - fromValuePercentage).toFloat()
                    )
                    val toPoint =
                        Offset(
                            x = size.width,
                            y = size.height.times(1 - toValuePercentage).toFloat()
                        )

                    val lineColor = if ((fromPoint.y - toPoint.y) >= 0) Color.Green else Color.Red

                    drawLine(
                        color = lineColor,
                        start = fromPoint,
                        end = toPoint,
                        strokeWidth = 5f
                    )

                    val verticalLineStart = Offset(
                        x = toPoint.x,
                        y = 0f
                    )
                    val verticalLineEnd = Offset(
                        x = toPoint.x,
                        y = size.height
                    )

                    drawLine(
                        color = Color.Gray,
                        start = verticalLineStart,
                        end = verticalLineEnd,
                        strokeWidth = 3f
                    )

                    drawLine(
                        color = Color.Gray,
                        start = Offset(0f, 0f),
                        end = Offset(0f, size.height),
                        strokeWidth = 3f
                    )


                    val lineSpacing = size.height / horizontalLineCount

                    for (i in 0..horizontalLineCount + 1) {
                        val y = i * lineSpacing

                        drawLine(
                            color = Color.Gray,
                            start = Offset(0f, y),
                            end = Offset(size.width, y),
                            strokeWidth = 1f
                        )

                    }

                })
        }
    }

}

@Preview
@Composable
private fun ChartPreview() {
    FintachartsTheme {
        Chart(
            Modifier.background(MaterialTheme.colorScheme.background),
            priceList = listOf(1.0, 8.0, 10.0, 4.0, 3.0, 6.0, 7.0, 1.0)
        )
    }
}

private fun getValuePercentageForRange(value: Double, max: Double, min: Double) =
    (value - min) / (max - min)