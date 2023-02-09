package com.example.moviejetpackcompose.ui.views

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.moviejetpackcompose.R
import com.example.moviejetpackcompose.ui.theme.RoundedShape
@Composable
fun MoviePoster(
    imagePath: String,
    size: Dp,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedShape,
        border = BorderStroke(0.5.dp, Color.Gray),
        modifier = modifier
            .height(size)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imagePath)
                .crossfade(true)
                .build(),
            placeholder = painterResource(R.drawable.placeholder),
            contentDescription = "movie poster",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize()
        )
    }
}
