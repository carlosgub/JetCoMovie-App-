package com.example.moviejetpackcompose.model.usecase

import com.example.moviejetpackcompose.data.SearchRepository
import com.example.moviejetpackcompose.ui.features.model.MovieModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetMoviesFromQueryUseCase @Inject constructor(
    private val searchRepository: SearchRepository
) {
    operator fun invoke(query: String): Flow<List<MovieModel>> = flow {
        emit(searchRepository.getMoviesFromQuery(query))
    }
}
