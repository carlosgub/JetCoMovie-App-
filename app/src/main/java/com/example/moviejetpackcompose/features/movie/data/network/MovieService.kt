package com.example.moviejetpackcompose.features.movie.data.network

import com.example.moviejetpackcompose.features.movie.data.network.response.CategoriesResponse
import com.example.moviejetpackcompose.features.movie.data.network.response.MovieResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MovieService @Inject constructor(
    private val movieClient: MovieClient
) {
    suspend fun getNowPlayingMovies(): List<MovieResponse> {
        return withContext(Dispatchers.IO) {
            val response = movieClient.getNowPlayingMovies()
            response.body()?.results ?: listOf()
        }
    }

    suspend fun getCategories(): List<CategoriesResponse> {
        return withContext(Dispatchers.IO) {
            val response = movieClient.getCategories()
            response.body()?.genres?.filter { it.name != null && it.id != null } ?: listOf()
        }
    }
}