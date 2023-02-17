package com.example.moviejetpackcompose.ui.features.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviejetpackcompose.core.DispatcherProvider
import com.example.moviejetpackcompose.core.sealed.GenericState
import com.example.moviejetpackcompose.model.usecase.BookingMovieUseCase
import com.example.moviejetpackcompose.model.usecase.DeleteBookingMovieUseCase
import com.example.moviejetpackcompose.model.usecase.GetMovieDetailUseCase
import com.example.moviejetpackcompose.model.usecase.IsMovieBookedUseCase
import com.example.moviejetpackcompose.ui.features.model.MovieModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getMovieDetailUseCase: GetMovieDetailUseCase,
    private val isMovieBookedUseCase: IsMovieBookedUseCase,
    private val bookingMovieUseCase: BookingMovieUseCase,
    private val deleteBookingMovieUseCase: DeleteBookingMovieUseCase,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    private val _uiState = MutableStateFlow<GenericState<MovieModel>>(GenericState.Loading)
    val uiState: StateFlow<GenericState<MovieModel>> = _uiState
    private val _bookingState = MutableStateFlow<GenericState<Boolean>>(GenericState.None)
    val bookingState: StateFlow<GenericState<Boolean>> = _bookingState

    fun getMovieDetail(movieId: String) {
        viewModelScope.launch(dispatcherProvider.main) {
            getMovieDetailUseCase(movieId)
                .flowOn(dispatcherProvider.io)
                .catch {
                    _uiState.value = GenericState.Error(it.message.orEmpty())
                }
                .collect {
                    _uiState.value = GenericState.Success(it)
                }
        }
    }

    fun isMovieBookedState(movieId: String) {
        viewModelScope.launch(dispatcherProvider.main) {
            isMovieBookedUseCase(movieId)
                .flowOn(dispatcherProvider.io)
                .catch {
                    _bookingState.value = GenericState.Error(it.message.orEmpty())
                }
                .collect {
                    _bookingState.value = GenericState.Success(it)
                }
        }
    }

    fun bookingMovie(movie: MovieModel) {
        viewModelScope.launch(Dispatchers.IO) {
            bookingMovieUseCase(movie)
        }
    }

    fun deleteBookingMovie(movie: MovieModel) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteBookingMovieUseCase(movie)
        }
    }
}
