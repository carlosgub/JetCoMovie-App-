package com.example.moviejetpackcompose.features.search.data.network

import com.example.moviejetpackcompose.features.movie.data.network.response.MovieResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SearchService @Inject constructor(
    private val searchClient: SearchClient
) {
    suspend fun getMoviesFromQuery(query: String): List<MovieResponse> {
        return withContext(Dispatchers.IO) {
            val response = searchClient.getMoviesFromQuery(query = query)
            response.body()?.results ?: listOf()
        }
    }
}