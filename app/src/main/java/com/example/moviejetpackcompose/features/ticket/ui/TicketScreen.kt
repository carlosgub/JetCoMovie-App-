package com.example.moviejetpackcompose.features.ticket.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.*
import com.example.moviejetpackcompose.R
import com.example.moviejetpackcompose.core.sealed.GenericState
import com.example.moviejetpackcompose.features.home.ui.model.BottomBarScreen
import com.example.moviejetpackcompose.features.movie.ui.model.MovieModel
import com.example.moviejetpackcompose.helpers.getDataFromUiState
import com.example.moviejetpackcompose.helpers.showLoading
import com.example.moviejetpackcompose.ui.theme.Gold
import com.example.moviejetpackcompose.ui.theme.GoldDarker
import com.example.moviejetpackcompose.ui.views.Loading
import com.example.moviejetpackcompose.ui.views.MoviePoster

@Composable
fun TicketScreen(
    viewModel: TicketViewModel,
    navController: NavController,
    mainNavController: NavHostController
) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val uiState by produceState<GenericState<List<MovieModel>>>(
        initialValue = GenericState.Loading,
        key1 = lifecycle,
        key2 = viewModel,
        producer = {
            lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    value = it
                }
            }
        }
    )

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val (loading, content) = createRefs()
        if (showLoading(uiState)) {
            Loading(
                modifier = Modifier.constrainAs(loading) {
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
        } else {
            TicketContent(
                list = getDataFromUiState(uiState) ?: listOf(),
                navController = navController,
                mainNavController = mainNavController,
                modifier = Modifier.constrainAs(content) {
                    linkTo(
                        start = parent.start,
                        end = parent.end
                    )
                    linkTo(
                        top = parent.top,
                        bottom = parent.bottom
                    )
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                }
            )

        }
    }
}

@Composable
fun TicketContent(
    list: List<MovieModel>,
    navController: NavController,
    mainNavController: NavHostController,
    modifier: Modifier
) {
    ConstraintLayout(
        modifier = modifier
    ) {
        val (noTickets, tickets) = createRefs()
        if (list.isEmpty()) {
            ShowNoTickets(
                modifier = Modifier
                    .constrainAs(noTickets) {
                        linkTo(
                            start = parent.start,
                            end = parent.end
                        )
                        linkTo(
                            top = parent.top,
                            bottom = parent.bottom
                        )
                        width = Dimension.fillToConstraints
                        height = Dimension.fillToConstraints
                    }
            ) {
                navController.navigate(BottomBarScreen.Movie.route)
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.padding(horizontal = 12.dp),
                contentPadding = PaddingValues(vertical = 12.dp)
            ) {
                items(list) { movieModel ->
                    MovieBookedItem(movieModel) {
                        mainNavController.navigate("detail/$it")
                    }
                }
            }
        }
    }
}

@Composable
fun ShowNoTickets(modifier: Modifier, onClick: () -> Unit) {
    ConstraintLayout(modifier = modifier) {
        val (lottie, spacer, button) = createRefs()
        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.ticket))
        val progressLottie by animateLottieCompositionAsState(
            composition,
            iterations = LottieConstants.IterateForever
        )
        LottieAnimation(
            composition = composition,
            progress = { progressLottie },
            modifier = Modifier
                .height(200.dp)
                .constrainAs(lottie) {
                    linkTo(
                        start = parent.start,
                        end = parent.end
                    )
                    linkTo(
                        top = parent.top,
                        bottom = spacer.top
                    )
                }
        )
        Spacer(
            modifier = Modifier
                .size(24.dp)
                .constrainAs(spacer) {
                    linkTo(
                        start = parent.start,
                        end = parent.end
                    )
                    linkTo(
                        top = lottie.bottom,
                        bottom = button.top
                    )
                }
        )
        Button(
            onClick = { onClick() },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.Transparent,
                contentColor = Color.White
            ),
            modifier = Modifier
                .constrainAs(button) {
                    linkTo(
                        start = parent.start,
                        end = parent.end
                    )
                    linkTo(
                        top = spacer.bottom,
                        bottom = parent.bottom
                    )
                }
        ) {
            Text(
                text = "Get tickets",
                modifier = Modifier
                    .background(
                        Brush.horizontalGradient(
                            0.0f to Gold,
                            0.5f to Gold,
                            1.0f to GoldDarker,
                            startX = 0f,
                            endX = Float.POSITIVE_INFINITY
                        ),
                        shape = CircleShape
                    )
                    .padding(
                        horizontal = 28.dp,
                        vertical = 16.dp
                    )
            )
        }
        createVerticalChain(lottie, spacer, button, chainStyle = ChainStyle.Packed)
    }
}

@Composable
fun MovieBookedItem(
    movieModel: MovieModel,
    goToMovieDetail: (Int) -> Unit
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 12.dp)
    ) {
        val (movieCard, text) = createRefs()
        MoviePoster(
            imagePath = movieModel.getImagePath(),
            size = 240.dp,
            modifier = Modifier
                .clickable {
                    goToMovieDetail(movieModel.id)
                }
                .constrainAs(movieCard) {
                    linkTo(
                        start = parent.start,
                        end = parent.end
                    )
                    top.linkTo(parent.top)
                }
        )
        Text(text = movieModel.originalTitle,
            textAlign = TextAlign.Center,
            color = Color.White,
            modifier = Modifier
                .clickable {
                    goToMovieDetail(movieModel.id)
                }
                .constrainAs(text) {
                    linkTo(
                        start = parent.start,
                        end = parent.end
                    )
                    linkTo(
                        top = movieCard.bottom,
                        topMargin = 8.dp,
                        bottom = parent.bottom,
                        bottomMargin = 8.dp
                    )
                })
    }
}