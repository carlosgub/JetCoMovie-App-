package com.example.moviejetpackcompose.features.search.model

import com.example.moviejetpackcompose.core.sealed.GenericState
import com.example.moviejetpackcompose.features.movie.ui.model.MovieModel
import com.example.moviejetpackcompose.features.search.data.SearchRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@OptIn(FlowPreview::class)
class GetMoviesFromQueryUseCase @Inject constructor(
    private val searchRepository: SearchRepository
) {
    operator fun invoke(query: Flow<String>): Flow<GenericState<List<MovieModel>>> = flow {
        query
            .debounce(500)
            .map { it.trim() }
            .distinctUntilChanged()
            .filter { it.length >= 3 }
            .collect { query ->
                emit(GenericState.Loading)
                emit(GenericState.Success(searchRepository.getMoviesFromQuery(query)))
            }
    }
}