package com.example.moviejetpackcompose.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.example.moviejetpackcompose.helpers.HALF_SCREEN
@Composable
fun BlackVerticalGradient(
    size: Dp,
    startColor: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(size)
            .background(
                Brush.verticalGradient(
                    0.0f to startColor,
                    HALF_SCREEN to startColor,
                    1.0f to Color.Transparent,
                    startY = Float.POSITIVE_INFINITY,
                    endY = 0f
                )
            )
    )
}
