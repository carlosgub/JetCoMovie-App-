package com.example.moviejetpackcompose.features.detail.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviejetpackcompose.core.sealed.GenericState
import com.example.moviejetpackcompose.features.detail.model.GetMovieDetailUseCase
import com.example.moviejetpackcompose.features.movie.ui.model.MovieModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getMovieDetailUseCase: GetMovieDetailUseCase
) : ViewModel() {

    lateinit var uiState: StateFlow<GenericState<MovieModel>>

    fun getMovieDetail(movieId:String){
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
}