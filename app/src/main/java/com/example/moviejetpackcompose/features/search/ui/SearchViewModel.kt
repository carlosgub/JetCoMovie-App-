package com.example.moviejetpackcompose.features.search.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviejetpackcompose.core.sealed.GenericState
import com.example.moviejetpackcompose.features.movie.ui.model.MovieModel
import com.example.moviejetpackcompose.features.search.model.GetMoviesFromQueryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    getMoviesFromQueryUseCase: GetMoviesFromQueryUseCase
) : ViewModel() {

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query

    val uiState: StateFlow<GenericState<List<MovieModel>>> =
        getMoviesFromQueryUseCase(query)
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


    fun queryFieldChange(query: String) {
        _query.value = query
    }
}