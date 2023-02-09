package com.example.moviejetpackcompose.ui.features.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.moviejetpackcompose.ui.features.model.BottomBarScreen
import com.example.moviejetpackcompose.ui.features.movie.MovieScreen
import com.example.moviejetpackcompose.ui.features.movie.MovieViewModel
import com.example.moviejetpackcompose.ui.features.profile.ProfileScreen
import com.example.moviejetpackcompose.ui.features.search.SearchScreen
import com.example.moviejetpackcompose.ui.features.search.SearchViewModel
import com.example.moviejetpackcompose.ui.features.ticket.TicketScreen
import com.example.moviejetpackcompose.ui.features.ticket.TicketViewModel
import com.example.moviejetpackcompose.ui.theme.ClearRed
import com.example.moviejetpackcompose.ui.theme.DividerColor
import com.example.moviejetpackcompose.ui.theme.Red
import com.example.moviejetpackcompose.ui.theme.divider_thickness
import com.example.moviejetpackcompose.ui.theme.myColors
import com.example.moviejetpackcompose.ui.theme.spacing_2
import com.example.moviejetpackcompose.ui.theme.spacing_2_2
import com.example.moviejetpackcompose.ui.theme.spacing_4_2
import com.example.moviejetpackcompose.ui.theme.view_10

@Composable
fun HomeScreen(
    movieViewModel: MovieViewModel,
    ticketViewModel: TicketViewModel,
    searchViewModel: SearchViewModel,
    mainNavController: NavHostController,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomBar(navController = navController) },
        backgroundColor = myColors.background,
        modifier = modifier
    ) { paddingValues ->
        BottomNavGraph(
            navController = navController,
            movieViewModel = movieViewModel,
            mainNavController = mainNavController,
            ticketViewModel = ticketViewModel,
            searchViewModel = searchViewModel,
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = paddingValues.calculateBottomPadding())
        )
    }
}

@Composable
fun BottomBar(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val navStackBackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navStackBackEntry?.destination

    ConstraintLayout(
        modifier = modifier
            .background(Color.Transparent)
            .fillMaxWidth()
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
            thickness = divider_thickness,
            color = DividerColor
        )

        AddItem(
            screen = BottomBarScreen.Movie,
            currentDestination = currentDestination,
            navController = navController,
            modifier = Modifier
                .constrainAs(movie) {
                    start.linkTo(parent.start, spacing_2_2)
                    end.linkTo(search.start)
                    linkTo(
                        top = parent.top,
                        topMargin = spacing_4_2,
                        bottomMargin = spacing_2,
                        bottom = parent.bottom
                    )
                }
        )
        AddItem(
            screen = BottomBarScreen.Search,
            currentDestination = currentDestination,
            navController = navController,
            modifier = Modifier.constrainAs(search) {
                start.linkTo(movie.end, spacing_2_2)
                end.linkTo(ticket.start)
                linkTo(
                    top = parent.top,
                    topMargin = spacing_4_2,
                    bottomMargin = spacing_2,
                    bottom = parent.bottom
                )
            }
        )
        AddItem(
            screen = BottomBarScreen.Ticket,
            currentDestination = currentDestination,
            navController = navController,
            modifier = Modifier.constrainAs(ticket) {
                start.linkTo(search.end, spacing_2_2)
                end.linkTo(profile.start)
                linkTo(
                    top = parent.top,
                    topMargin = spacing_4_2,
                    bottomMargin = spacing_2,
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
                end.linkTo(parent.end, spacing_2_2)
                linkTo(
                    top = parent.top,
                    topMargin = spacing_4_2,
                    bottomMargin = spacing_2,
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
    modifier: Modifier = Modifier
) {
    val selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true

    val background =
        if (selected) {
            Brush.horizontalGradient(
                colors = listOf(
                    ClearRed,
                    Red
                ),
                startX = 0f,
                endX = Float.POSITIVE_INFINITY
            )
        } else {
            Brush.horizontalGradient(
                colors = listOf(
                    Color.Transparent,
                    Color.Transparent
                )
            )
        }

    val contentColor =
        if (selected) Color.White else Color.Gray

    ConstraintLayout(
        modifier = modifier
            .height(view_10)
            .clip(CircleShape)
            .background(background)
            .padding(
                start = spacing_2_2,
                end = spacing_2_2,
                top = spacing_2,
                bottom = spacing_2
            )
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
                    startMargin = spacing_2,
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
    mainNavController: NavHostController,
    movieViewModel: MovieViewModel,
    ticketViewModel: TicketViewModel,
    searchViewModel: SearchViewModel,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = BottomBarScreen.Movie.route,
        modifier = modifier
    ) {
        composable(route = BottomBarScreen.Movie.route) {
            MovieScreen(
                viewModel = movieViewModel,
                navController = mainNavController
            )
        }
        composable(route = BottomBarScreen.Search.route) {
            SearchScreen(
                viewModel = searchViewModel,
                mainNavController = mainNavController
            )
        }
        composable(route = BottomBarScreen.Ticket.route) {
            TicketScreen(
                viewModel = ticketViewModel,
                navController = navController,
                mainNavController = mainNavController
            )
        }
        composable(route = BottomBarScreen.Profile.route) {
            ProfileScreen()
        }
    }
}
