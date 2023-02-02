package com.example.moviejetpackcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.moviejetpackcompose.features.detail.ui.DetailScreen
import com.example.moviejetpackcompose.features.detail.ui.DetailViewModel
import com.example.moviejetpackcompose.features.home.ui.HomeScreen
import com.example.moviejetpackcompose.features.movie.ui.MovieViewModel
import com.example.moviejetpackcompose.ui.theme.MovieJetpackComposeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val movieViewModel: MovieViewModel by viewModels()
        val detailViewModel: DetailViewModel by viewModels()
        setContent {
            MovieJetpackComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "home") {
                        composable(
                            route = "home",
                            content = {
                                HomeScreen(movieViewModel, navController)
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
                                    detailViewModel,
                                    navController,
                                    backStackEntry.arguments?.getInt("id") ?: 0
                                )
                            }
                        )
                    }

                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MovieJetpackComposeTheme {
        Greeting("Android")
    }
}