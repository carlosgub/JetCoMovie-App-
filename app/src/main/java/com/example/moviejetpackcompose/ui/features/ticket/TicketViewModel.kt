package com.example.moviejetpackcompose.ui.features.ticket

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviejetpackcompose.core.sealed.GenericState
import com.example.moviejetpackcompose.ui.features.model.MovieModel
import com.example.moviejetpackcompose.usecase.GetMoviesBookedUseCase
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