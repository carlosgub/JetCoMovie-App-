package com.example.moviejetpackcompose.ui.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.moviejetpackcompose.ui.features.model.MovieModel
import com.example.moviejetpackcompose.ui.theme.myColors
import com.example.moviejetpackcompose.ui.theme.spacing_2
import com.example.moviejetpackcompose.ui.theme.spacing_3
import com.example.moviejetpackcompose.ui.theme.view_60
import kotlinx.collections.immutable.ImmutableList

@Composable
fun LazyVerticalGridMovies(
    list: ImmutableList<MovieModel>,
    modifier: Modifier = Modifier,
    state: LazyGridState = rememberLazyGridState(),
    contentPaddingValues: PaddingValues = PaddingValues(vertical = spacing_3),
    itemClicked: (it: Int) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.padding(horizontal = spacing_3),
        contentPadding = contentPaddingValues,
        state = state
    ) {
        items(list) { movieModel ->
            MovieBookedItem(movieModel) {
                itemClicked(it)
            }
        }
    }
}

@Composable
fun MovieBookedItem(
    movieModel: MovieModel,
    modifier: Modifier = Modifier,
    goToMovieDetail: (Int) -> Unit
) {
    ConstraintLayout(
        modifier = modifier
            .fillMaxWidth()
            .padding(all = spacing_3)
    ) {
        val (movieCard, text) = createRefs()
        MoviePoster(
            imagePath = movieModel.getImagePath(),
            size = view_60,
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
        Text(
            text = movieModel.originalTitle,
            textAlign = TextAlign.Center,
            color = myColors.surface,
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
                        topMargin = spacing_2,
                        bottom = parent.bottom,
                        bottomMargin = spacing_2
                    )
                }
        )
    }
}
