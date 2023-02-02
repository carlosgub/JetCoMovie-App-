@file:OptIn(ExperimentalPagerApi::class)

package com.example.moviejetpackcompose.features.movie.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.GraphicsLayerScope
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import androidx.constraintlayout.compose.ChainStyle
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
import com.example.moviejetpackcompose.ui.views.CategoryChip
import com.example.moviejetpackcompose.ui.views.Loading
import com.google.accompanist.pager.*
import kotlin.math.absoluteValue

@Composable
fun MovieScreen(
    viewModel: MovieViewModel,
    navController: NavController
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
        modifier = Modifier.fillMaxSize()
    ) {
        val (pager, progress) = createRefs()
        val pagerState = rememberPagerState()
        val list = getDataFromUiState(uiState) ?: listOf()

        if (showLoading(uiState)) {
            Loading(
                modifier = Modifier.constrainAs(progress) {
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
            HorizontalPager(count = list.size,
                state = pagerState,
                contentPadding = PaddingValues(horizontal = 50.dp),
                modifier = Modifier
                    .constrainAs(pager) {
                        linkTo(
                            start = parent.start,
                            end = parent.end
                        )
                        top.linkTo(parent.top, 24.dp)
                    }
            ) { page ->
                val pageOffset = calculateCurrentOffsetForPage(page).absoluteValue
                MovieItem(pageOffset, pagerState, page, movieModel = list[page]) {
                    navController.navigate("detail/$it")
                }
            }
        }

    }
}

@Composable
fun MovieItem(
    pageOffset: Float,
    pagerState: PagerState,
    page: Int,
    movieModel: MovieModel,
    goToMovieDetail: (Int) -> Unit
) {
    ConstraintLayout(
        modifier = Modifier.fillMaxSize()
    ) {
        val (movieCard, textMovieContainer) = createRefs()
        MoviePoster(
            pageOffset = pageOffset,
            movieModel = movieModel,
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
        AnimatedVisibility(
            visible = pagerState.currentPage == page,
            enter = fadeIn(
                animationSpec = spring(stiffness = Spring.StiffnessVeryLow)
            ),
            exit = fadeOut(
                animationSpec = spring(stiffness = Spring.StiffnessVeryLow)
            ),
            modifier = Modifier
                .constrainAs(textMovieContainer) {
                    linkTo(
                        start = parent.start,
                        end = parent.end
                    )
                    linkTo(
                        top = movieCard.bottom,
                        topMargin = 18.dp,
                        bottom = parent.bottom,
                    )
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                }
                .graphicsLayer {
                    animationMovieItem(pageOffset)
                }
        ) {
            ConstraintLayout(modifier = Modifier.fillMaxSize()) {
                val (movieText, categoriesChips) = createRefs()
                MovieText(
                    movieModel = movieModel,
                    modifier = Modifier
                        .constrainAs(movieText) {
                            linkTo(
                                start = parent.start,
                                end = parent.end
                            )
                            top.linkTo(parent.top)
                            width = Dimension.fillToConstraints
                        }
                )

                MovieCategories(
                    categories = movieModel.categories,
                    modifier = Modifier
                        .constrainAs(categoriesChips) {
                            linkTo(
                                start = parent.start,
                                end = parent.end
                            )
                            top.linkTo(movieText.bottom, 24.dp)
                            bottom.linkTo(parent.bottom, 12.dp)
                            width = Dimension.fillToConstraints
                            height = Dimension.fillToConstraints
                        }
                )
            }
        }

    }
}

@Composable
fun MoviePoster(pageOffset: Float, movieModel: MovieModel, modifier: Modifier) {
    Card(
        shape = MaterialTheme.shapes.medium.copy(CornerSize(25.dp)),
        border = BorderStroke(0.5.dp, Color.Gray),
        modifier = modifier
            .height(450.dp)
            .graphicsLayer {
                animationMovieItem(pageOffset)
            }
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(movieModel.getImagePath())
                .crossfade(true)
                .build(),
            placeholder = painterResource(R.drawable.placeholder),
            contentDescription = "movie poster",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
fun MovieText(
    movieModel: MovieModel,
    modifier: Modifier
) {
    ConstraintLayout(modifier = modifier) {
        val (movieTimeIcon, movieTimeSpacer, movieTimeText, movieTitle) = createRefs()
        Icon(
            imageVector = Icons.Outlined.CalendarMonth,
            contentDescription = "time",
            tint = Color.White,
            modifier = Modifier
                .size(24.dp)
                .constrainAs(movieTimeIcon) {
                    top.linkTo(parent.top)
                    linkTo(
                        start = parent.start,
                        end = movieTimeText.start
                    )
                }
        )
        Spacer(
            modifier = Modifier
                .size(8.dp)
                .constrainAs(movieTimeSpacer) {
                    linkTo(
                        top = movieTimeIcon.top,
                        bottom = movieTimeIcon.bottom
                    )
                    linkTo(
                        start = movieTimeIcon.end,
                        end = movieTimeText.start
                    )
                }
        )
        Text(
            text = movieModel.releaseDate.orEmpty(),
            color = Color.White,
            modifier = Modifier
                .constrainAs(movieTimeText) {
                    linkTo(
                        top = movieTimeIcon.top,
                        bottom = movieTimeIcon.bottom
                    )
                    linkTo(
                        start = movieTimeSpacer.end,
                        end = parent.end
                    )
                }
        )
        createHorizontalChain(
            movieTimeIcon,
            movieTimeSpacer,
            movieTimeText,
            chainStyle = ChainStyle.Packed(0.4f)
        )

        Text(
            text = movieModel.originalTitle,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .constrainAs(movieTitle) {
                    top.linkTo(movieTimeIcon.bottom, 16.dp)

                    linkTo(
                        start = parent.start,
                        end = parent.end
                    )
                }
        )

    }
}

@Composable
fun MovieCategories(
    categories: List<String>,
    modifier: Modifier
) {
    ConstraintLayout(modifier = modifier.fillMaxSize()) {
        val (firstCategory, secondCategory) = createRefs()
        CategoryChip(category = categories.firstOrNull(),
            modifier = Modifier.constrainAs(firstCategory) {
                linkTo(
                    start = parent.start,
                    startMargin = 16.dp,
                    end = secondCategory.start,
                    endMargin = 8.dp,
                    endGoneMargin = 16.dp
                )
            })
        CategoryChip(category = categories.getOrNull(1),
            modifier = Modifier.constrainAs(secondCategory) {
                linkTo(
                    start = firstCategory.end,
                    startMargin = 8.dp,
                    end = parent.end,
                    endMargin = 16.dp
                )
            })
    }
}


//This code is from https://google.github.io/accompanist/pager/#item-scroll-effects
private fun GraphicsLayerScope.animationMovieItem(pageOffset: Float) {
    lerp(
        start = 0.85f,
        stop = 1f,
        fraction = 1f - pageOffset.coerceIn(0f, 1f)
    ).also { scale ->
        scaleX = scale
        scaleY = scale
    }

    alpha = lerp(
        start = 0.5f,
        stop = 1f,
        fraction = 1f - pageOffset.coerceIn(0f, 1f)
    )
}

