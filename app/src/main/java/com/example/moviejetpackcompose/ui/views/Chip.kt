package com.example.moviejetpackcompose.ui.views

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.moviejetpackcompose.ui.theme.BlackChip
import com.example.moviejetpackcompose.ui.theme.RoundedShape
import com.example.moviejetpackcompose.ui.theme.myColors
import com.example.moviejetpackcompose.ui.theme.spacing_2_2
import com.example.moviejetpackcompose.ui.theme.spacing_4

@Composable
fun CategoryChip(category: String?, modifier: Modifier = Modifier) {
    Card(
        shape = RoundedShape,
        backgroundColor = BlackChip,
        modifier = modifier
    ) {
        Text(
            text = category.orEmpty(),
            style = MaterialTheme.typography.body2,
            color = myColors.chip,
            modifier = Modifier.padding(
                horizontal = spacing_4,
                vertical = spacing_2_2
            )
        )
    }
}
