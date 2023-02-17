package com.example.moviejetpackcompose.ui.features.ticket

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviejetpackcompose.core.DispatcherProvider
import com.example.moviejetpackcompose.core.sealed.GenericState
import com.example.moviejetpackcompose.model.usecase.GetMoviesBookedUseCase
import com.example.moviejetpackcompose.ui.features.model.MovieModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TicketViewModel @Inject constructor(
    private val getMoviesBookedUseCase: GetMoviesBookedUseCase,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    private val _uiState = MutableStateFlow<GenericState<List<MovieModel>>>(GenericState.Loading)
    val uiState: StateFlow<GenericState<List<MovieModel>>> = _uiState

    init {
        getTickets()
    }

    private fun getTickets() {
        viewModelScope.launch(dispatcherProvider.main) {
            getMoviesBookedUseCase()
                .flowOn(dispatcherProvider.io)
                .catch {
                    _uiState.value = GenericState.Error(it.message.orEmpty())
                }
                .collect {
                    _uiState.value = GenericState.Success(it)
                }
        }
    }
}
