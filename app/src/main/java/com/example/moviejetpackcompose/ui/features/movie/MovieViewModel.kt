package com.example.moviejetpackcompose.ui.features.movie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviejetpackcompose.core.sealed.GenericState
import com.example.moviejetpackcompose.helpers.TIMEOUT_FLOW
import com.example.moviejetpackcompose.model.usecase.GetNowPlayingMoviesUseCase
import com.example.moviejetpackcompose.ui.features.model.MovieModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    getNowPlayingMoviesUseCase: GetNowPlayingMoviesUseCase
) : ViewModel() {

    val uiState: StateFlow<GenericState<List<MovieModel>>> by lazy {
        getNowPlayingMoviesUseCase()
            .map {
                try {
                    GenericState.Success(it)
                } catch (e: Exception) {
                    GenericState.Error(e.message.orEmpty())
                }
            }
            .catch {
                GenericState.Error(it.message.orEmpty())
            }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(TIMEOUT_FLOW),
                GenericState.Loading
            )
    }
}
