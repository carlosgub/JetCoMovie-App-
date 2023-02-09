package com.example.moviejetpackcompose.ui.features.movie

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.GraphicsLayerScope
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.util.lerp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.constraintlayout.compose.Visibility
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import com.example.moviejetpackcompose.core.sealed.GenericState
import com.example.moviejetpackcompose.helpers.ONE_THIRDS_SCREEN
import com.example.moviejetpackcompose.helpers.TWO_THIRDS_SCREEN
import com.example.moviejetpackcompose.helpers.getDataFromUiState
import com.example.moviejetpackcompose.helpers.showLoading
import com.example.moviejetpackcompose.ui.features.model.MovieModel
import com.example.moviejetpackcompose.ui.theme.myColors
import com.example.moviejetpackcompose.ui.theme.spacing_10
import com.example.moviejetpackcompose.ui.theme.spacing_2
import com.example.moviejetpackcompose.ui.theme.spacing_3
import com.example.moviejetpackcompose.ui.theme.spacing_4
import com.example.moviejetpackcompose.ui.theme.spacing_4_2
import com.example.moviejetpackcompose.ui.theme.spacing_6
import com.example.moviejetpackcompose.ui.theme.view_0
import com.example.moviejetpackcompose.ui.theme.view_2
import com.example.moviejetpackcompose.ui.theme.view_6
import com.example.moviejetpackcompose.ui.views.CategoryChip
import com.example.moviejetpackcompose.ui.views.Loading
import com.example.moviejetpackcompose.ui.views.MoviePoster
import com.example.moviejetpackcompose.ui.views.MovieTitle
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.calculateCurrentOffsetForPage
import com.google.accompanist.pager.rememberPagerState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlin.math.absoluteValue

@Composable
fun MovieScreen(
    viewModel: MovieViewModel,
    navController: NavController,
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
        modifier = modifier.fillMaxSize()
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
            HorizontalPager(
                count = list.size,
                state = pagerState,
                contentPadding = PaddingValues(horizontal = spacing_10),
                modifier = Modifier
                    .constrainAs(pager) {
                        linkTo(
                            start = parent.start,
                            end = parent.end
                        )
                        top.linkTo(parent.top, spacing_6)
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
    modifier: Modifier = Modifier,
    goToMovieDetail: (Int) -> Unit
) {
    ConstraintLayout(
        modifier = modifier.fillMaxSize()
    ) {
        val (movieCard, textMovieContainer) = createRefs()
        MoviePoster(
            imagePath = movieModel.getImagePath(),
            size = view_0,
            modifier = Modifier
                .fillMaxHeight(TWO_THIRDS_SCREEN)
                .clickable {
                    goToMovieDetail(movieModel.id)
                }
                .graphicsLayer {
                    animationMovieItem(pageOffset)
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
                .fillMaxHeight(ONE_THIRDS_SCREEN)
                .constrainAs(textMovieContainer) {
                    linkTo(
                        start = parent.start,
                        end = parent.end
                    )
                    linkTo(
                        top = movieCard.bottom,
                        topMargin = spacing_4_2,
                        bottom = parent.bottom
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
                    categories = movieModel.categories.toImmutableList(),
                    modifier = Modifier
                        .constrainAs(categoriesChips) {
                            linkTo(
                                start = parent.start,
                                end = parent.end
                            )
                            top.linkTo(movieText.bottom, spacing_6)
                            bottom.linkTo(parent.bottom, spacing_3)
                            width = Dimension.fillToConstraints
                            height = Dimension.fillToConstraints
                        }
                )
            }
        }
    }
}

@Composable
fun MovieText(
    movieModel: MovieModel,
    modifier: Modifier = Modifier
) {
    ConstraintLayout(modifier = modifier) {
        val (movieTimeIcon, movieTimeSpacer, movieTimeText, movieTitle) = createRefs()
        Icon(
            imageVector = Icons.Outlined.CalendarMonth,
            contentDescription = "time",
            tint = myColors.iconTint,
            modifier = Modifier
                .size(view_6)
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
                .size(view_2)
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
            color = myColors.surface,
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
            chainStyle = ChainStyle.Packed
        )

        MovieTitle(
            title = movieModel.originalTitle,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .constrainAs(movieTitle) {
                    top.linkTo(movieTimeIcon.bottom, spacing_4)

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
    categories: ImmutableList<String>,
    modifier: Modifier = Modifier
) {
    ConstraintLayout(modifier = modifier.fillMaxSize()) {
        val (firstCategoryRef, secondCategoryRef) = createRefs()

        val firstCategory = categories.firstOrNull()
        CategoryChip(
            category = firstCategory,
            modifier = Modifier.constrainAs(firstCategoryRef) {
                linkTo(
                    start = parent.start,
                    startMargin = spacing_4,
                    end = secondCategoryRef.start,
                    endMargin = spacing_2,
                    endGoneMargin = spacing_4
                )
                visibility =
                    if (firstCategory != null) Visibility.Visible else Visibility.Gone
            }
        )

        val secondCategory = categories.getOrNull(1)
        CategoryChip(
            category = secondCategory,
            modifier = Modifier.constrainAs(secondCategoryRef) {
                linkTo(
                    start = firstCategoryRef.end,
                    startMargin = spacing_2,
                    end = parent.end,
                    endMargin = spacing_4
                )
                visibility =
                    if (secondCategory != null) Visibility.Visible else Visibility.Gone
            }
        )
    }
}

// This code is from https://google.github.io/accompanist/pager/#item-scroll-effects
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
