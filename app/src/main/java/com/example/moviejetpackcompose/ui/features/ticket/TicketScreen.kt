package com.example.moviejetpackcompose.ui.features.ticket

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.moviejetpackcompose.R
import com.example.moviejetpackcompose.core.sealed.GenericState
import com.example.moviejetpackcompose.helpers.HALF_SCREEN
import com.example.moviejetpackcompose.helpers.getDataFromUiState
import com.example.moviejetpackcompose.helpers.showLoading
import com.example.moviejetpackcompose.ui.features.model.BottomBarScreen
import com.example.moviejetpackcompose.ui.features.model.MovieModel
import com.example.moviejetpackcompose.ui.theme.Gold
import com.example.moviejetpackcompose.ui.theme.GoldDarker
import com.example.moviejetpackcompose.ui.theme.buttonNoElevation
import com.example.moviejetpackcompose.ui.theme.spacing_4
import com.example.moviejetpackcompose.ui.theme.spacing_7
import com.example.moviejetpackcompose.ui.theme.view_50
import com.example.moviejetpackcompose.ui.theme.view_6
import com.example.moviejetpackcompose.ui.views.LazyVerticalGridMovies
import com.example.moviejetpackcompose.ui.views.Loading
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

@Composable
fun TicketScreen(
    viewModel: TicketViewModel,
    navController: NavController,
    mainNavController: NavHostController,
    modifier: Modifier = Modifier
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
        modifier = modifier
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
                list = getDataFromUiState(uiState)?.toImmutableList() ?: persistentListOf(),
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
    list: ImmutableList<MovieModel>,
    navController: NavController,
    mainNavController: NavHostController,
    modifier: Modifier = Modifier
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
            LazyVerticalGridMovies(
                list = list.toImmutableList(),
                modifier = Modifier.constrainAs(tickets) {
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
                mainNavController.navigate("detail/$it")
            }
        }
    }
}

@Composable
fun ShowNoTickets(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
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
                .height(view_50)
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
                .size(view_6)
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
            elevation = buttonNoElevation,
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
                            HALF_SCREEN to Gold,
                            1.0f to GoldDarker,
                            startX = 0f,
                            endX = Float.POSITIVE_INFINITY
                        ),
                        shape = CircleShape
                    )
                    .padding(
                        horizontal = spacing_7,
                        vertical = spacing_4
                    )
            )
        }
        createVerticalChain(lottie, spacer, button, chainStyle = ChainStyle.Packed)
    }
}
