package com.example.moviejetpackcompose.ui.features.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalActivity
import androidx.compose.material.icons.filled.LocalMovies
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomBarScreen(
    val route: String,
    val title: String,
    val icon: ImageVector
) {

    // for home
    object Movie : BottomBarScreen(
        route = "movie",
        title = "Movie",
        icon = Icons.Filled.LocalMovies
    )

    // for search
    object Search : BottomBarScreen(
        route = "search",
        title = "Search",
        icon = Icons.Filled.Search
    )

    // for ticket
    object Ticket : BottomBarScreen(
        route = "ticket",
        title = "Ticket",
        icon = Icons.Filled.LocalActivity
    )

    // for profile
    object Profile : BottomBarScreen(
        route = "profile",
        title = "Profile",
        icon = Icons.Outlined.AccountCircle
    )
}
