@file:OptIn(ExperimentalAnimationApi::class)

package com.example.moviejetpackcompose.features.detail.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocalActivity
import androidx.compose.material.icons.rounded.Star
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.moviejetpackcompose.R
import com.example.moviejetpackcompose.core.sealed.GenericState
import com.example.moviejetpackcompose.features.movie.ui.model.MovieModel
import com.example.moviejetpackcompose.helpers.getDataFromUiState
import com.example.moviejetpackcompose.helpers.showLoading
import com.example.moviejetpackcompose.ui.theme.ClearRed
import com.example.moviejetpackcompose.ui.theme.Gold
import com.example.moviejetpackcompose.ui.theme.GoldDarker
import com.example.moviejetpackcompose.ui.theme.Red
import com.example.moviejetpackcompose.ui.views.BlackVerticalGradient
import com.example.moviejetpackcompose.ui.views.CategoryChip
import com.example.moviejetpackcompose.ui.views.Loading
import java.math.RoundingMode

@Composable
fun DetailScreen(
    viewModel: DetailViewModel,
    navController: NavController,
    id: Int
) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val uiState by produceState<GenericState<MovieModel>>(
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

    val bookingState by produceState<GenericState<Boolean>>(
        initialValue = GenericState.Loading,
        key1 = lifecycle,
        key2 = viewModel,
        producer = {
            lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
                viewModel.bookingState.collect {
                    value = it
                }
            }
        }
    )

    viewModel.getMovieDetail(id.toString())
    viewModel.isMovieBookedState(id.toString())

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        val (loading, content, booking) = createRefs()
        if (showLoading(uiState)) {
            Loading(modifier = Modifier.constrainAs(loading) {
                linkTo(
                    start = parent.start,
                    end = parent.end
                )
                linkTo(
                    top = parent.top,
                    bottom = parent.bottom
                )
            })
        } else {
            getDataFromUiState(uiState)?.let { movieModel ->
                MovieDetailContent(
                    movie = movieModel,
                    navController = navController,
                    modifier = Modifier.constrainAs(content) {
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
                val isMovieBooked = getDataFromUiState(bookingState) ?: false
                Booking(
                    isMovieBooked,
                    Modifier
                        .constrainAs(booking) {
                            linkTo(
                                start = parent.start,
                                end = parent.end
                            )
                            bottom.linkTo(parent.bottom)
                            width = Dimension.fillToConstraints
                        }
                ) {
                    if (isMovieBooked) {
                        viewModel.deleteBookingMovie(movieModel)
                    } else {
                        viewModel.bookingMovie(movieModel)
                    }
                }
            }

        }
    }
}

@Composable
fun MovieDetailContent(
    movie: MovieModel,
    navController: NavController,
    modifier: Modifier
) {

    val state = rememberScrollState()
    ConstraintLayout(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(state)
    ) {
        val (close, time, poster, textContent) = createRefs()
        MoviePosterDetail(
            path = movie.getImagePath(),
            modifier = Modifier.constrainAs(poster) {
                top.linkTo(parent.top)
                linkTo(
                    start = parent.start,
                    end = parent.end
                )
            }
        )
        CloseMovie(
            navController = navController,
            modifier = Modifier.constrainAs(close) {
                top.linkTo(parent.top, 24.dp)
                start.linkTo(parent.start, 24.dp)
            }
        )
        movie.runtime?.let {
            MovieDuration(
                duration = it,
                modifier = Modifier.constrainAs(time) {
                    end.linkTo(parent.end, 24.dp)
                    top.linkTo(parent.top, 24.dp)
                }
            )
        }
        MovieTextContent(
            movie = movie,
            modifier = Modifier
                .constrainAs(textContent) {
                    linkTo(
                        top = poster.bottom,
                        topMargin = (-150).dp,
                        bottom = parent.bottom,
                        bias = 0f
                    )
                    linkTo(
                        start = parent.start,
                        end = parent.end
                    )
                    width = Dimension.fillToConstraints
                    height = Dimension.wrapContent
                }
        )
    }
}

@Composable
fun CloseMovie(navController: NavController, modifier: Modifier) {
    Icon(
        imageVector = Icons.Filled.Close,
        contentDescription = "Close screen",
        tint = Color.White,
        modifier = modifier
            .clip(CircleShape)
            .background(
                Color.Gray.copy(
                    alpha = 0.6f
                )
            )
            .padding(8.dp)
            .clickable {
                navController.popBackStack()
            }
    )
}

@Composable
fun MovieDuration(duration: String, modifier: Modifier) {
    ConstraintLayout(
        modifier = modifier
            .clip(CircleShape)
            .background(
                Color.Gray.copy(
                    alpha = 0.6f
                )
            )
            .padding(
                vertical = 6.dp,
                horizontal = 12.dp
            )
    ) {
        val (icon, text) = createRefs()
        Icon(
            imageVector = Icons.Filled.AccessTime,
            contentDescription = "Movie Time",
            tint = Color.White,
            modifier = Modifier.constrainAs(icon) {
                start.linkTo(parent.start)
                linkTo(
                    top = parent.top,
                    bottom = parent.bottom
                )
            }
        )
        Text(
            text = duration,
            color = Color.White,
            modifier = Modifier.constrainAs(text) {
                start.linkTo(icon.end, 4.dp)
                end.linkTo(parent.end)
                linkTo(
                    top = icon.top,
                    bottom = icon.bottom
                )
            }
        )
    }
}

@Composable
fun MoviePosterDetail(path: String, modifier: Modifier) {
    ConstraintLayout(modifier = modifier.fillMaxWidth()) {
        val (poster, gradient) = createRefs()
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(path)
                .crossfade(true)
                .build(),
            placeholder = painterResource(R.drawable.placeholder),
            contentDescription = "movie poster",
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(poster) {
                    top.linkTo(parent.top)
                    linkTo(
                        start = parent.start,
                        end = parent.end
                    )
                }
        )
        BlackVerticalGradient(
            size = 300.dp,
            modifier = Modifier.constrainAs(gradient) {
                bottom.linkTo(parent.bottom)
                linkTo(
                    start = parent.start,
                    end = parent.end
                )
                width = Dimension.fillToConstraints
            }
        )
    }
}

@Composable
fun MovieTextContent(movie: MovieModel, modifier: Modifier) {
    ConstraintLayout(
        modifier = modifier
            .fillMaxSize()
    ) {
        val (IMDB, ranking, title, categories, overview) = createRefs()
        IMDBRanking(
            modifier = Modifier
                .constrainAs(IMDB) {
                    start.linkTo(parent.start, 24.dp)
                    top.linkTo(parent.top, 16.dp)
                }
        )
        VoteMovie(
            voteAverage = movie.voteAverage ?: 0.0,
            voteCount = movie.voteCount,
            modifier = Modifier
                .constrainAs(ranking) {
                    start.linkTo(IMDB.end, 8.dp)
                    end.linkTo(parent.end, 24.dp)
                    top.linkTo(IMDB.top)
                    bottom.linkTo(IMDB.bottom)
                    width = Dimension.fillToConstraints
                }
        )
        MovieDetailTitle(
            title = movie.originalTitle,
            modifier = Modifier
                .constrainAs(title) {
                    linkTo(
                        start = parent.start,
                        startMargin = 24.dp,
                        end = parent.end,
                        endMargin = 24.dp
                    )
                    top.linkTo(IMDB.bottom, 8.dp)
                    width = Dimension.fillToConstraints
                }
        )
        CategoriesChipsDetail(
            list = movie.categories,
            modifier = Modifier
                .constrainAs(categories) {
                    linkTo(
                        start = parent.start,
                        end = parent.end,
                    )
                    top.linkTo(title.bottom, 12.dp)
                    width = Dimension.fillToConstraints
                }
        )
        Text(
            text = "${movie.overview.orEmpty()} ${movie.overview.orEmpty()}",
            textAlign = TextAlign.Justify,
            color = Color.White,
            fontSize = 14.sp,
            modifier = Modifier
                .constrainAs(overview) {
                    linkTo(
                        start = parent.start,
                        startMargin = 24.dp,
                        end = parent.end,
                        endMargin = 24.dp
                    )
                    top.linkTo(categories.bottom, 24.dp)
                    bottom.linkTo(parent.bottom, 100.dp)
                    width = Dimension.fillToConstraints
                    height = Dimension.wrapContent
                }
        )
    }
}

@Composable
fun IMDBRanking(modifier: Modifier) {
    Text(
        text = "IMDB 7.0",
        color = Color.Black,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        modifier = modifier
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
                horizontal = 12.dp,
                vertical = 4.dp
            )
    )
}

@Composable
fun VoteMovie(
    voteAverage: Double,
    voteCount: Int,
    modifier: Modifier
) {
    ConstraintLayout(modifier = modifier) {
        val (icon, voteAverageRef, voteCountRef) = createRefs()
        Icon(
            imageVector = Icons.Rounded.Star,
            contentDescription = "Star ranking",
            tint = Gold,
            modifier = Modifier
                .size(24.dp)
                .constrainAs(icon) {
                    start.linkTo(parent.start, 4.dp)
                    linkTo(
                        top = parent.top,
                        bottom = parent.bottom
                    )
                }
        )
        Text(
            text = voteAverage.toBigDecimal().setScale(1, RoundingMode.CEILING).toString(),
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Gold,
            modifier = Modifier
                .constrainAs(voteAverageRef) {
                    start.linkTo(icon.end, 4.dp)
                    linkTo(
                        top = icon.top,
                        bottom = icon.bottom
                    )
                }
        )
        Text(
            text = "(${voteCount} reviews)",
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .constrainAs(voteCountRef) {
                    linkTo(
                        start = voteAverageRef.end,
                        startMargin = 8.dp,
                        end = parent.end,
                        endMargin = 24.dp
                    )
                    linkTo(
                        top = icon.top,
                        bottom = icon.bottom
                    )
                    width = Dimension.fillToConstraints
                }
        )
    }
}

@Composable
fun MovieDetailTitle(title: String, modifier: Modifier) {
    Text(
        text = title,
        color = Color.White,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        modifier = modifier
    )
}

@Composable
fun CategoriesChipsDetail(list: List<String>, modifier: Modifier) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 24.dp)
    ) {
        items(list) { category ->
            CategoryChip(
                category = category
            )
        }
    }
}

@Composable
fun Booking(
    isMovieBooked: Boolean,
    modifier: Modifier,
    onClick: () -> Unit
) {
    ConstraintLayout(
        modifier = modifier.fillMaxWidth()
    ) {
        val (button, gradient) = createRefs()
        BlackVerticalGradient(
            size = 100.dp,
            modifier = Modifier
                .constrainAs(gradient) {
                    bottom.linkTo(parent.bottom)
                    linkTo(
                        start = parent.start,
                        end = parent.end
                    )
                    width = Dimension.fillToConstraints
                }
        )

        Button(
            onClick = {
                onClick()
            },
            colors = ButtonDefaults.buttonColors(
                contentColor = Color.White,
                backgroundColor = Color.Transparent
            ),
            modifier = Modifier
                .constrainAs(button) {
                    linkTo(
                        top = parent.top,
                        bottom = parent.bottom
                    )
                    linkTo(
                        start = parent.start,
                        end = parent.end
                    )
                    width = Dimension.wrapContent
                }
        ) {
            ConstraintLayout(
                Modifier
                    .background(
                        Brush.horizontalGradient(
                            0.0f to ClearRed,
                            0.5f to Red,
                            1.0f to Red,
                            startX = 0f,
                            endX = Float.POSITIVE_INFINITY
                        ),
                        shape = CircleShape
                    )
            ) {
                val (icon, text) = createRefs()
                Icon(
                    imageVector = Icons.Filled.LocalActivity,
                    contentDescription = "booking",
                    modifier = Modifier
                        .constrainAs(icon) {
                            linkTo(
                                top = parent.top,
                                topMargin = 8.dp,
                                bottom = parent.bottom,
                                bottomMargin = 8.dp
                            )
                            start.linkTo(parent.start, 20.dp)
                        }
                )
                AnimatedContent(
                    targetState = isMovieBooked,
                    modifier = Modifier
                        .constrainAs(text) {
                            linkTo(
                                top = parent.top,
                                topMargin = 8.dp,
                                bottom = parent.bottom,
                                bottomMargin = 8.dp
                            )
                            linkTo(
                                start = icon.end,
                                startMargin = 8.dp,
                                end = parent.end,
                                endMargin = 20.dp
                            )
                        }
                ) { targetState ->
                    Text(
                        text = if (targetState) "Remove Booking" else "Booking",
                        fontSize = 20.sp
                    )
                }

            }
        }
    }
}

