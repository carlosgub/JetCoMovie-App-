package com.example.moviejetpackcompose.features.search.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.constraintlayout.compose.ConstraintLayout

@Composable
fun SearchScreen() {
    ConstraintLayout(
        modifier = Modifier.fillMaxSize()
    ) {
        val (text) = createRefs()
        Text(
            text = "Search",
            color = Color.White,
            modifier = Modifier
                .constrainAs(text) {
                    linkTo(
                        start = parent.start,
                        end = parent.end
                    )
                    linkTo(
                        top = parent.top,
                        bottom = parent.bottom
                    )
                }
        )
    }
}