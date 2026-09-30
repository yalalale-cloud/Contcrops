package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.ContGreen
import com.example.ui.theme.ContRed

@Composable
fun SparklineChart(
    data: List<Int>,
    isUp: Boolean,
    modifier: Modifier = Modifier,
    width: Dp = 60.dp,
    height: Dp = 24.dp
) {
    if (data.size < 2) return

    val strokeColor = if (isUp) ContGreen else ContRed
    val fillColor = if (isUp) ContGreen.copy(alpha = 0.15f) else ContRed.copy(alpha = 0.15f)

    Canvas(
        modifier = modifier
            .width(width)
            .height(height)
    ) {
        val minVal = data.minOrNull()?.toFloat() ?: 0f
        val maxVal = data.maxOrNull()?.toFloat() ?: 1f
        val range = if (maxVal - minVal == 0f) 1f else maxVal - minVal

        val w = size.width
        val h = size.height

        val stepX = w / (data.size - 1)

        val path = Path()
        val fillPath = Path()

        data.forEachIndexed { index, value ->
            val x = index * stepX
            val normalizedY = 1f - ((value - minVal) / range)
            val y = normalizedY * (h - 8f) + 4f

            if (index == 0) {
                path.moveTo(x, y)
                fillPath.moveTo(x, h)
                fillPath.lineTo(x, y)
            } else {
                path.lineTo(x, y)
                fillPath.lineTo(x, y)
            }
        }

        fillPath.lineTo(w, h)
        fillPath.close()

        drawPath(
            path = fillPath,
            brush = Brush.verticalGradient(
                colors = listOf(fillColor, Color.Transparent),
                startY = 0f,
                endY = h
            )
        )

        drawPath(
            path = path,
            color = strokeColor,
            style = Stroke(
                width = 2.dp.toPx(),
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
        )

        // Draw last point dot
        val lastY = (1f - ((data.last() - minVal) / range)) * (h - 8f) + 4f
        drawCircle(
            color = strokeColor,
            radius = 2.5.dp.toPx(),
            center = Offset(w, lastY)
        )
    }
}
