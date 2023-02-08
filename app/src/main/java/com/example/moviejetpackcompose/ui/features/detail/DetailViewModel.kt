package com.example.moviejetpackcompose.ui.features.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviejetpackcompose.core.sealed.GenericState
import com.example.moviejetpackcompose.model.usecase.BookingMovieUseCase
import com.example.moviejetpackcompose.model.usecase.DeleteBookingMovieUseCase
import com.example.moviejetpackcompose.model.usecase.GetMovieDetailUseCase
import com.example.moviejetpackcompose.model.usecase.IsMovieBookedUseCase
import com.example.moviejetpackcompose.ui.features.model.MovieModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getMovieDetailUseCase: GetMovieDetailUseCase,
    private val isMovieBookedUseCase: IsMovieBookedUseCase,
    private val bookingMovieUseCase: BookingMovieUseCase,
    private val deleteBookingMovieUseCase: DeleteBookingMovieUseCase
) : ViewModel() {

    lateinit var uiState: StateFlow<GenericState<MovieModel>>
    lateinit var bookingState: StateFlow<GenericState<Boolean>>

    fun getMovieDetail(movieId: String) {
        uiState = getMovieDetailUseCase(movieId)
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

    fun isMovieBookedState(movieId: String) {
        bookingState = isMovieBookedUseCase(movieId)
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
