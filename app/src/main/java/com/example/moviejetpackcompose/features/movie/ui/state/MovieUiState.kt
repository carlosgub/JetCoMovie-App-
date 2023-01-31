package com.example.moviejetpackcompose.features.movie.ui.state

import com.example.moviejetpackcompose.features.movie.ui.model.MovieModel

sealed interface MovieUiState {
    object Loading : MovieUiState
    data class Error(val message: String) : MovieUiState
    data class Success(val movies: List<MovieModel>) : MovieUiState
}