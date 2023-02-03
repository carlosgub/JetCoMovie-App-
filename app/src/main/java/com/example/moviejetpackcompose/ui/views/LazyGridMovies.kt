package com.example.moviejetpackcompose.ui.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.moviejetpackcompose.features.movie.ui.model.MovieModel

@Composable
fun LazyVerticalGridMovies(
    list: List<MovieModel>,
    modifier: Modifier,
    state: LazyGridState = rememberLazyGridState(),
    contentPaddingValues: PaddingValues = PaddingValues(vertical = 12.dp),
    itemClicked: (it: Int) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.padding(horizontal = 12.dp),
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
