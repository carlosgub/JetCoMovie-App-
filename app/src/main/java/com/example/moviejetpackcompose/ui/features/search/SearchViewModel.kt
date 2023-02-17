package com.example.moviejetpackcompose.ui.features.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.example.moviejetpackcompose.core.DispatcherProvider
import com.example.moviejetpackcompose.core.sealed.GenericState
import com.example.moviejetpackcompose.helpers.MINIMUM_CHARACTERS_TO_SEARCH
import com.example.moviejetpackcompose.model.usecase.GetMoviesFromQueryUseCase
import com.example.moviejetpackcompose.ui.features.model.MovieModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getMoviesFromQueryUseCase: GetMoviesFromQueryUseCase,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    private val _query = MutableLiveData<String>()
    val query: LiveData<String> = _query

    private val _uiState = MutableStateFlow<GenericState<List<MovieModel>>>(GenericState.None)
    val uiState: StateFlow<GenericState<List<MovieModel>>> = _uiState

    init {
        search()
    }

    fun queryFieldChange(query: String) {
        if (query.length >= MINIMUM_CHARACTERS_TO_SEARCH) {
            _uiState.value = GenericState.Loading
        }
        _query.value = query
    }

    fun search() {
        viewModelScope.launch(dispatcherProvider.main) {
            _query
                .asFlow()
                .flowOn(dispatcherProvider.io)
                .filter {
                    it.trim().isEmpty().not() && it.length >= MINIMUM_CHARACTERS_TO_SEARCH
                }
                .debounce(DEBOUNCE_TIME)
                .distinctUntilChanged()
                .flatMapLatest {
                    getMoviesFromQueryUseCase(it)
                }
                .catch {
                    _uiState.value = GenericState.Error(it.message.orEmpty())
                }
                .collect {
                    _uiState.value = GenericState.Success(it)
                }
        }
    }

    companion object {
        const val DEBOUNCE_TIME = 300L
    }
}
