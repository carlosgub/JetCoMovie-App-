package com.example.moviejetpackcompose.ui.views

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.example.moviejetpackcompose.ui.theme.Typography
import com.example.moviejetpackcompose.ui.theme.myColors

@Composable
fun MovieTitle(
    title: String,
    modifier: Modifier = Modifier,
    textAlign: TextAlign? = null
) {
    Text(
        text = title,
        style = Typography.h4,
        color = myColors.surface,
        textAlign = textAlign,
        modifier = modifier
    )
}
