package com.example.moviejetpackcompose.data.network.service

import com.example.moviejetpackcompose.data.network.clients.SearchClient
import com.example.moviejetpackcompose.data.network.response.MovieResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SearchService @Inject constructor(
    private val searchClient: SearchClient
) {
    suspend fun getMoviesFromQuery(query: String): List<MovieResponse> =
        withContext(Dispatchers.IO) {
            val response = searchClient.getMoviesFromQuery(query = query)
            response.body()?.results ?: listOf()
        }
}
