package com.example.moviejetpackcompose.ui.features.detail

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.moviejetpackcompose.R
import com.example.moviejetpackcompose.core.sealed.GenericState
import com.example.moviejetpackcompose.helpers.HALF_SCREEN
import com.example.moviejetpackcompose.helpers.getDataFromUiState
import com.example.moviejetpackcompose.helpers.showLoading
import com.example.moviejetpackcompose.ui.features.model.MovieModel
import com.example.moviejetpackcompose.ui.theme.ClearRed
import com.example.moviejetpackcompose.ui.theme.Gold
import com.example.moviejetpackcompose.ui.theme.GoldDarker
import com.example.moviejetpackcompose.ui.theme.Red
import com.example.moviejetpackcompose.ui.theme.buttonNoElevation
import com.example.moviejetpackcompose.ui.theme.myColors
import com.example.moviejetpackcompose.ui.theme.spacing_1
import com.example.moviejetpackcompose.ui.theme.spacing_1_2
import com.example.moviejetpackcompose.ui.theme.spacing_2
import com.example.moviejetpackcompose.ui.theme.spacing_25
import com.example.moviejetpackcompose.ui.theme.spacing_2_2
import com.example.moviejetpackcompose.ui.theme.spacing_3
import com.example.moviejetpackcompose.ui.theme.spacing_37_2
import com.example.moviejetpackcompose.ui.theme.spacing_4
import com.example.moviejetpackcompose.ui.theme.spacing_6
import com.example.moviejetpackcompose.ui.theme.view_25
import com.example.moviejetpackcompose.ui.theme.view_6
import com.example.moviejetpackcompose.ui.theme.view_75
import com.example.moviejetpackcompose.ui.views.BlackVerticalGradient
import com.example.moviejetpackcompose.ui.views.CategoryChip
import com.example.moviejetpackcompose.ui.views.Loading
import com.example.moviejetpackcompose.ui.views.MovieTitle
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import java.math.RoundingMode

@Composable
fun DetailScreen(
    viewModel: DetailViewModel,
    navController: NavController,
    id: Int,
    modifier: Modifier = Modifier
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
        modifier = modifier
            .fillMaxSize()
            .background(myColors.background)
    ) {
        val (loading, content, booking) = createRefs()
        if (showLoading(uiState)) {
            Loading(
                modifier = Modifier.constrainAs(
                    loading
                ) {
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
    modifier: Modifier = Modifier
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
                top.linkTo(parent.top, spacing_6)
                start.linkTo(parent.start, spacing_6)
            }
        )
        movie.runtime?.let {
            MovieDuration(
                duration = it,
                modifier = Modifier.constrainAs(time) {
                    end.linkTo(parent.end, spacing_6)
                    top.linkTo(parent.top, spacing_6)
                }
            )
        }
        MovieTextContent(
            movie = movie,
            modifier = Modifier
                .constrainAs(textContent) {
                    linkTo(
                        top = poster.bottom,
                        topMargin = -spacing_37_2,
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
fun CloseMovie(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    Icon(
        imageVector = Icons.Filled.Close,
        contentDescription = "Close screen",
        tint = myColors.iconTint,
        modifier = modifier
            .clip(CircleShape)
            .background(
                myColors.background.copy(
                    alpha = 0.6f
                )
            )
            .padding(spacing_2)
            .clickable {
                navController.popBackStack()
            }
    )
}

@Composable
fun MovieDuration(
    duration: String,
    modifier: Modifier = Modifier
) {
    ConstraintLayout(
        modifier = modifier
            .clip(CircleShape)
            .background(
                myColors.background.copy(
                    alpha = 0.6f
                )
            )
            .padding(
                vertical = spacing_1_2,
                horizontal = spacing_3
            )
    ) {
        val (icon, text) = createRefs()
        Icon(
            imageVector = Icons.Filled.AccessTime,
            contentDescription = "Movie Time",
            tint = myColors.iconTint,
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
            color = myColors.surface,
            modifier = Modifier.constrainAs(text) {
                start.linkTo(icon.end, spacing_1)
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
fun MoviePosterDetail(
    path: String,
    modifier: Modifier = Modifier
) {
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
            size = view_75,
            startColor = myColors.background,
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
fun MovieTextContent(
    movie: MovieModel,
    modifier: Modifier = Modifier
) {
    ConstraintLayout(
        modifier = modifier
            .fillMaxSize()
    ) {
        val (IMDB, ranking, title, categories, overview) = createRefs()
        IMDBRanking(
            modifier = Modifier
                .constrainAs(IMDB) {
                    start.linkTo(parent.start, spacing_6)
                    top.linkTo(parent.top, spacing_4)
                }
        )
        VoteMovie(
            voteAverage = movie.voteAverage ?: 0.0,
            voteCount = movie.voteCount,
            modifier = Modifier
                .constrainAs(ranking) {
                    start.linkTo(IMDB.end, spacing_2)
                    end.linkTo(parent.end, spacing_6)
                    top.linkTo(IMDB.top)
                    bottom.linkTo(IMDB.bottom)
                    width = Dimension.fillToConstraints
                }
        )
        MovieTitle(
            title = movie.originalTitle,
            modifier = Modifier
                .constrainAs(title) {
                    linkTo(
                        start = parent.start,
                        startMargin = spacing_6,
                        end = parent.end,
                        endMargin = spacing_6
                    )
                    top.linkTo(IMDB.bottom, spacing_2)
                    width = Dimension.fillToConstraints
                }
        )

        CategoriesChipsDetail(
            list = movie.categories.toImmutableList(),
            modifier = Modifier
                .constrainAs(categories) {
                    linkTo(
                        start = parent.start,
                        end = parent.end
                    )
                    top.linkTo(title.bottom, spacing_3)
                    width = Dimension.fillToConstraints
                }
        )
        Text(
            text = "${movie.overview.orEmpty()} ${movie.overview.orEmpty()}",
            textAlign = TextAlign.Justify,
            style = MaterialTheme.typography.body2,
            color = myColors.surface,
            modifier = Modifier
                .constrainAs(overview) {
                    linkTo(
                        start = parent.start,
                        startMargin = spacing_6,
                        end = parent.end,
                        endMargin = spacing_6
                    )
                    top.linkTo(categories.bottom, spacing_6)
                    bottom.linkTo(parent.bottom, spacing_25)
                    width = Dimension.fillToConstraints
                    height = Dimension.wrapContent
                }
        )
    }
}

@Composable
fun IMDBRanking(modifier: Modifier = Modifier) {
    Text(
        text = "IMDB 7.0",
        style = MaterialTheme.typography.body2,
        color = Color.Black,
        modifier = modifier
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
                horizontal = spacing_3,
                vertical = spacing_1
            )
    )
}

@Composable
fun VoteMovie(
    voteAverage: Double,
    voteCount: Int,
    modifier: Modifier = Modifier
) {
    ConstraintLayout(modifier = modifier) {
        val (icon, voteAverageRef, voteCountRef) = createRefs()
        Icon(
            imageVector = Icons.Rounded.Star,
            contentDescription = "Star ranking",
            tint = Gold,
            modifier = Modifier
                .size(view_6)
                .constrainAs(icon) {
                    start.linkTo(parent.start, spacing_1)
                    linkTo(
                        top = parent.top,
                        bottom = parent.bottom
                    )
                }
        )
        Text(
            text = voteAverage.toBigDecimal().setScale(1, RoundingMode.CEILING).toString(),
            style = MaterialTheme.typography.h6,
            color = Gold,
            modifier = Modifier
                .constrainAs(voteAverageRef) {
                    start.linkTo(icon.end, spacing_1)
                    linkTo(
                        top = icon.top,
                        bottom = icon.bottom
                    )
                }
        )
        Text(
            text = "($voteCount reviews)",
            style = MaterialTheme.typography.caption,
            color = myColors.surface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .constrainAs(voteCountRef) {
                    linkTo(
                        start = voteAverageRef.end,
                        startMargin = spacing_2,
                        end = parent.end,
                        endMargin = spacing_6
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
fun CategoriesChipsDetail(list: ImmutableList<String>, modifier: Modifier = Modifier) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(spacing_2_2),
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = spacing_6)
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
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    ConstraintLayout(
        modifier = modifier.fillMaxWidth()
    ) {
        val (button, gradient) = createRefs()
        BlackVerticalGradient(
            size = view_25,
            startColor = myColors.background,
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
            elevation = buttonNoElevation,
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
                            HALF_SCREEN to Red,
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
                                topMargin = spacing_2,
                                bottom = parent.bottom,
                                bottomMargin = spacing_2
                            )
                            start.linkTo(parent.start, spacing_6)
                        }
                )
                AnimatedContent(
                    targetState = isMovieBooked,
                    modifier = Modifier
                        .constrainAs(text) {
                            linkTo(
                                top = parent.top,
                                topMargin = spacing_2,
                                bottom = parent.bottom,
                                bottomMargin = spacing_2
                            )
                            linkTo(
                                start = icon.end,
                                startMargin = spacing_2,
                                end = parent.end,
                                endMargin = spacing_6
                            )
                        }
                ) { targetState ->
                    Text(
                        text = if (targetState) "Remove Booking" else "Booking",
                        style = MaterialTheme.typography.button
                    )
                }
            }
        }
    }
}
