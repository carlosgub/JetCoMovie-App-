package com.example.moviejetpackcompose.model.usecase

import com.example.moviejetpackcompose.data.DetailRepository
import com.example.moviejetpackcompose.ui.features.model.MovieModel
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
