package com.example.moviejetpackcompose.features.detail.model

import com.example.moviejetpackcompose.features.detail.data.DetailRepository
import com.example.moviejetpackcompose.features.movie.ui.model.MovieModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetMovieDetailUseCase @Inject constructor(
    private val detailRepository: DetailRepository
) {
    operator fun invoke(movieId: String): Flow<MovieModel> = flow {
        emit(detailRepository.getMovieDetail(movieId))
    }
}