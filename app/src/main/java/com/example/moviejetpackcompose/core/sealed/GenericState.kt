package com.example.moviejetpackcompose.core.sealed

sealed class GenericState<out T> {
    object Loading : GenericState<Nothing>()
    object None : GenericState<Nothing>()
    data class Error(val message: String) : GenericState<Nothing>()
    data class Success<T>(val item: T) : GenericState<T>()
}
