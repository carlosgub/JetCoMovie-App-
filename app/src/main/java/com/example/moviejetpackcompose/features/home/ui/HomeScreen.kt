package com.example.moviejetpackcompose.features.home.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.moviejetpackcompose.features.home.ui.model.BottomBarScreen
import com.example.moviejetpackcompose.features.movie.ui.MovieScreen
import com.example.moviejetpackcompose.features.movie.ui.MovieViewModel
import com.example.moviejetpackcompose.features.profile.ui.ProfileScreen
import com.example.moviejetpackcompose.features.search.ui.SearchScreen
import com.example.moviejetpackcompose.features.ticket.ui.TicketScreen
import com.example.moviejetpackcompose.ui.theme.ClearRed
import com.example.moviejetpackcompose.ui.theme.Red

@Composable
fun HomeScreen(movieViewModel: MovieViewModel) {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomBar(navController = navController) },
        backgroundColor = Color.Black
    ) {
        Modifier.padding(it)
        BottomNavGraph(
            navController = navController,
            movieViewModel = movieViewModel
        )
    }
}

@Composable
fun BottomBar(navController: NavHostController) {

    val navStackBackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navStackBackEntry?.destination

    ConstraintLayout(
        modifier = Modifier
            .background(Color.Transparent)
            .fillMaxWidth(),
    ) {
        val (movie, search, ticket, profile, divider) = createRefs()

        Divider(
            modifier = Modifier
                .constrainAs(divider) {
                    linkTo(
                        start = parent.start,
                        end = parent.end
                    )
                    top.linkTo(parent.top)
                },
            thickness = 0.25.dp,
            color = Color.Gray
        )

        AddItem(
            screen = BottomBarScreen.Movie,
            currentDestination = currentDestination,
            navController = navController,
            modifier = Modifier
                .constrainAs(movie) {
                    start.linkTo(parent.start)
                    end.linkTo(search.start)
                    linkTo(
                        top = parent.top,
                        topMargin = 18.dp,
                        bottomMargin = 8.dp,
                        bottom = parent.bottom,
                    )
                }
        )
        AddItem(
            screen = BottomBarScreen.Search,
            currentDestination = currentDestination,
            navController = navController,
            modifier = Modifier.constrainAs(search) {
                start.linkTo(movie.end, 10.dp)
                end.linkTo(ticket.start)
                linkTo(
                    top = parent.top,
                    topMargin = 18.dp,
                    bottomMargin = 8.dp,
                    bottom = parent.bottom
                )
            }
        )
        AddItem(
            screen = BottomBarScreen.Ticket,
            currentDestination = currentDestination,
            navController = navController,
            modifier = Modifier.constrainAs(ticket) {
                start.linkTo(search.end)
                end.linkTo(profile.start)
                linkTo(
                    top = parent.top,
                    topMargin = 18.dp,
                    bottomMargin = 8.dp,
                    bottom = parent.bottom
                )
            }
        )
        AddItem(
            screen = BottomBarScreen.Profile,
            currentDestination = currentDestination,
            navController = navController,
            modifier = Modifier.constrainAs(profile) {
                start.linkTo(ticket.end)
                end.linkTo(parent.end, 10.dp)
                linkTo(
                    top = parent.top,
                    topMargin = 18.dp,
                    bottomMargin = 8.dp,
                    bottom = parent.bottom
                )
            }
        )
    }
}

@Composable
fun AddItem(
    screen: BottomBarScreen,
    currentDestination: NavDestination?,
    navController: NavHostController,
    modifier: Modifier
) {
    val selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true

    val background =
        if (selected) Brush.horizontalGradient(
            colors = listOf(
                ClearRed,
                Red
            ),
            startX = 0f,
            endX = Float.POSITIVE_INFINITY
        ) else Brush.horizontalGradient(
            colors = listOf(
                Color.Transparent,
                Color.Transparent
            )
        )

    val contentColor =
        if (selected) Color.White else Color.Gray

    ConstraintLayout(
        modifier = modifier
            .height(40.dp)
            .clip(CircleShape)
            .background(background)
            .padding(start = 10.dp, end = 10.dp, top = 8.dp, bottom = 8.dp)
            .clickable(onClick = {
                navController.navigate(screen.route) {
                    popUpTo(navController.graph.findStartDestination().id)
                    launchSingleTop = true
                }
            })
    ) {
        val (icon, text) = createRefs()
        Icon(
            imageVector = screen.icon,
            contentDescription = "icon",
            tint = contentColor,
            modifier = Modifier.constrainAs(icon) {
                linkTo(
                    start = parent.start,
                    end = text.start
                )
            }
        )
        AnimatedVisibility(
            visible = selected,
            modifier = Modifier.constrainAs(text) {
                linkTo(
                    start = icon.end,
                    startMargin = 8.dp,
                    end = parent.end
                )
                linkTo(
                    top = parent.top,
                    bottom = parent.bottom
                )
            }
        ) {
            Text(
                text = screen.title,
                color = contentColor
            )
        }
    }
}

@Composable
fun BottomNavGraph(
    navController: NavHostController,
    movieViewModel: MovieViewModel
) {
    NavHost(
        navController = navController,
        startDestination = BottomBarScreen.Movie.route,
    ) {

        composable(route = BottomBarScreen.Movie.route) {
            MovieScreen(movieViewModel)
        }
        composable(route = BottomBarScreen.Search.route) {
            SearchScreen()
        }
        composable(route = BottomBarScreen.Ticket.route) {
            TicketScreen()
        }
        composable(route = BottomBarScreen.Profile.route) {
            ProfileScreen()
        }
    }
}