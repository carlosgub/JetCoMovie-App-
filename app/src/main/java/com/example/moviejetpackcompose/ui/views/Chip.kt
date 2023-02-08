package com.example.moviejetpackcompose.ui.views

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.moviejetpackcompose.ui.theme.*

@Composable
fun CategoryChip(category: String?, modifier: Modifier = Modifier) {
    Card(
        shape = RoundedShape,
        backgroundColor = BlackChip,
        modifier = modifier
    ) {
        Text(
            text = category.orEmpty(),
            style = Typography.body2,
            color = myColors.chip,
            modifier = Modifier.padding(
                horizontal = spacing_4,
                vertical = spacing_2_2
            )
        )
    }
}
