package com.example.moviejetpackcompose.ui.views

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moviejetpackcompose.ui.theme.BlackChip

@Composable
fun CategoryChip(category: String?, modifier: Modifier = Modifier) {
    Card(
        shape = RoundedCornerShape(24.dp),
        backgroundColor = BlackChip,
        modifier = modifier
    ) {
        Text(
            text = category.orEmpty(),
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color.White,
            modifier = Modifier.padding(
                horizontal = 16.dp,
                vertical = 10.dp
            )
        )
    }

}