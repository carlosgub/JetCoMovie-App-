package com.example.moviejetpackcompose.helpers

import com.example.moviejetpackcompose.core.sealed.GenericState

fun <T> showLoading(
    uiState: GenericState<T>
): Boolean = when (uiState) {
    is GenericState.Error -> false
    GenericState.Loading -> true
    GenericState.None -> false
    is GenericState.Success -> false
}

fun <T> getDataFromUiState(
    uiState: GenericState<T>
): T? =
    if (uiState is GenericState.Success) {
        uiState.item
    } else {
        null
    }
