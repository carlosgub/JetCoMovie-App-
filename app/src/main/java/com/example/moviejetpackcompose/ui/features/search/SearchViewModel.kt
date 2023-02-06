@file:OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)

package com.example.moviejetpackcompose.ui.features.search

import androidx.lifecycle.*
import com.example.moviejetpackcompose.core.sealed.GenericState
import com.example.moviejetpackcompose.ui.features.model.MovieModel
import com.example.moviejetpackcompose.model.usecase.GetMoviesFromQueryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
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
                it.trim().isEmpty().not() && it.length >= 3
            }
            .debounce(300)
            .distinctUntilChanged()
            .flatMapLatest {
                getMoviesFromQueryUseCase(it)
            }
            .map {
                _loading.value = false
                GenericState.Success(it)
            }.catch {
                GenericState.Error(it.message.orEmpty())
            }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                GenericState.Loading
            )

    fun queryFieldChange(query: String) {
        if (query.length >= 3) _loading.value = true
        _query.value = query
    }
}