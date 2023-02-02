package com.example.moviejetpackcompose.features.ticket.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviejetpackcompose.core.sealed.GenericState
import com.example.moviejetpackcompose.features.movie.ui.model.MovieModel
import com.example.moviejetpackcompose.features.ticket.model.GetMoviesBookedUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class TicketViewModel @Inject constructor(
    getMoviesBookedUseCase: GetMoviesBookedUseCase,
) : ViewModel() {

    val uiState: StateFlow<GenericState<List<MovieModel>>> = getMoviesBookedUseCase()
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