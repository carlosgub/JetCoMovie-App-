package com.example.moviejetpackcompose.features.movie.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviejetpackcompose.core.sealed.GenericState
import com.example.moviejetpackcompose.features.movie.model.GetNowPlayingMoviesUseCase
import com.example.moviejetpackcompose.features.movie.ui.model.MovieModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    getNowPlayingMoviesUseCase: GetNowPlayingMoviesUseCase
) : ViewModel() {

    val uiState: StateFlow<GenericState<List<MovieModel>>> = getNowPlayingMoviesUseCase()
        .map {
            GenericState.Success(it)
        }
        .catch {
            GenericState.Error(it.message.orEmpty())
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            GenericState.Loading
        )
}