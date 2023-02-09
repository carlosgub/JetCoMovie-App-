package com.example.moviejetpackcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.moviejetpackcompose.ui.features.detail.DetailScreen
import com.example.moviejetpackcompose.ui.features.detail.DetailViewModel
import com.example.moviejetpackcompose.ui.features.home.HomeScreen
import com.example.moviejetpackcompose.ui.features.movie.MovieViewModel
import com.example.moviejetpackcompose.ui.features.search.SearchViewModel
import com.example.moviejetpackcompose.ui.features.ticket.TicketViewModel
import com.example.moviejetpackcompose.ui.theme.MovieJetpackComposeTheme
import com.example.moviejetpackcompose.ui.theme.myColors
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val movieViewModel: MovieViewModel by viewModels()
        val detailViewModel: DetailViewModel by viewModels()
        val ticketViewModel: TicketViewModel by viewModels()
        val searchViewModel: SearchViewModel by viewModels()
        setContent {
            MovieJetpackComposeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = myColors.background
                ) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "home") {
                        composable(
                            route = "home",
                            content = {
                                HomeScreen(
                                    movieViewModel = movieViewModel,
                                    ticketViewModel = ticketViewModel,
                                    searchViewModel = searchViewModel,
                                    mainNavController = navController
                                )
                            }
                        )
                        composable(
                            route = "detail/{id}",
                            arguments = listOf(
                                navArgument("id") {
                                    type = NavType.IntType
                                }
                            ),
                            content = { backStackEntry ->
                                DetailScreen(
                                    viewModel = detailViewModel,
                                    navController = navController,
                                    id = backStackEntry.arguments?.getInt("id") ?: 0
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}
