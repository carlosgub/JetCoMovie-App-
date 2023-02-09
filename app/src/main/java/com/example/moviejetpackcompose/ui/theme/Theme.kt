package com.example.moviejetpackcompose.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ButtonElevation
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val DarkColorPalette = MyColors(
    material = darkColors(
        primary = ClearRed,
        primaryVariant = Red,
        secondary = Teal200,
        background = Color.Black,
        surface = Color.White
    ),
    chip = Color.White,
    iconTint = Color.White
)

private val LightColorPalette = MyColors(
    material = lightColors(
        primary = ClearRed,
        primaryVariant = Red,
        secondary = Teal200,
        background = Color.White,
        surface = Color.Black
    ),
    chip = Color.White,
    iconTint = Color.Black
)

private val LocalColors = staticCompositionLocalOf { LightColorPalette }

@Composable
fun MovieJetpackComposeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    CompositionLocalProvider(LocalColors provides colors) {
        MaterialTheme(
            colors = colors.material,
            typography = Typography,
            shapes = Shapes,
            content = content
        )
    }
}

val buttonNoElevation: ButtonElevation
    @Composable
    get() = ButtonDefaults.elevation(
        defaultElevation = 0.dp,
        pressedElevation = 0.dp,
        disabledElevation = 0.dp,
        hoveredElevation = 0.dp,
        focusedElevation = 0.dp
    )

val myColors: MyColors
    @Composable
    @ReadOnlyComposable
    get() = LocalColors.current
