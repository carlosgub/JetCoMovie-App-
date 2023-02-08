package com.example.moviejetpackcompose.model.usecase

import com.example.moviejetpackcompose.data.MovieRepository
import com.example.moviejetpackcompose.ui.features.model.MovieModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetNowPlayingMoviesUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    operator fun invoke(): Flow<List<MovieModel>> = flow {
        emit(repository.getNowPlayingMovies())
    }
}
