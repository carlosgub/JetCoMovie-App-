package com.example.moviejetpackcompose.ui.features.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.example.moviejetpackcompose.core.sealed.GenericState
import com.example.moviejetpackcompose.helpers.MINIMUM_CHARACTERS_TO_SEARCH
import com.example.moviejetpackcompose.helpers.TIMEOUT_FLOW
import com.example.moviejetpackcompose.model.usecase.GetMoviesFromQueryUseCase
import com.example.moviejetpackcompose.ui.features.model.MovieModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    getMoviesFromQueryUseCase: GetMoviesFromQueryUseCase
) : ViewModel() {

    private val _query = MutableLiveData<String>()
    val query: LiveData<String> = _query

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    val uiState: StateFlow<GenericState<List<MovieModel>>> =
        _query
            .asFlow()
            .filter {
                it.trim().isEmpty().not() && it.length >= MINIMUM_CHARACTERS_TO_SEARCH
            }
            .debounce(300)
            .distinctUntilChanged()
            .flatMapLatest {
                getMoviesFromQueryUseCase(it)
            }
            .map {
                _loading.value = false
                GenericState.Success(it)
            }
            .catch {
                GenericState.Error(it.message.orEmpty())
            }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(TIMEOUT_FLOW),
                GenericState.Loading
            )

    fun queryFieldChange(query: String) {
        if (query.length >= MINIMUM_CHARACTERS_TO_SEARCH) _loading.value = true
        _query.value = query
    }
}
