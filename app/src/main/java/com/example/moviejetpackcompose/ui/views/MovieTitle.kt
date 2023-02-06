package com.example.moviejetpackcompose.ui.views

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.example.moviejetpackcompose.ui.theme.Typography

@Composable
fun MovieTitle(title: String, modifier: Modifier, textAlign: TextAlign? = null) {
    Text(
        text = title,
        style = Typography.h4,
        textAlign = textAlign,
        modifier = modifier
    )
}