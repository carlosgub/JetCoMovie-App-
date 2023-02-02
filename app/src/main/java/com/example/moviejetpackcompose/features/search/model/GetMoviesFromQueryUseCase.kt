package com.example.moviejetpackcompose.features.search.model

import com.example.moviejetpackcompose.features.movie.ui.model.MovieModel
import com.example.moviejetpackcompose.features.search.data.SearchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetMoviesFromQueryUseCase @Inject constructor(
    private val searchRepository: SearchRepository
) {
    operator fun invoke(query: Flow<String>): Flow<List<MovieModel>> = flow {
        query.collect { query ->
            if (query.length >= 3)
                emit(searchRepository.getMoviesFromQuery(query))
        }
    }
}