package com.example.moviejetpackcompose.features.movie.model

import com.example.moviejetpackcompose.features.movie.data.MovieRepository
import com.example.moviejetpackcompose.features.movie.data.network.response.MovieResponse
import com.example.moviejetpackcompose.features.movie.ui.model.MovieModel
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