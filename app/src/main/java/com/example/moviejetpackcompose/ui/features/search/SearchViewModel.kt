package com.example.moviejetpackcompose.ui.features.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviejetpackcompose.core.sealed.GenericState
import com.example.moviejetpackcompose.ui.features.model.MovieModel
import com.example.moviejetpackcompose.usecase.GetMoviesFromQueryUseCase
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
                it
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