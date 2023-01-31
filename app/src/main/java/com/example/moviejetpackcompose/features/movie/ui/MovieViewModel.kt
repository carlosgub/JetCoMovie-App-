package com.example.moviejetpackcompose.features.movie.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviejetpackcompose.features.movie.model.GetNowPlayingMoviesUseCase
import com.example.moviejetpackcompose.features.movie.ui.state.MovieUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    getNowPlayingMoviesUseCase: GetNowPlayingMoviesUseCase
) : ViewModel() {

    val uiState: StateFlow<MovieUiState> = getNowPlayingMoviesUseCase()
        .map {
            MovieUiState.Success(it) as MovieUiState
        }
        .catch {
            MovieUiState.Error(it.message.orEmpty()) as MovieUiState
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            MovieUiState.Loading
        )
}