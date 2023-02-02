package com.example.moviejetpackcompose.features.search.data

import com.example.moviejetpackcompose.features.movie.ui.model.MovieModel
import com.example.moviejetpackcompose.features.search.data.network.SearchService
import com.example.moviejetpackcompose.helpers.toMovieModel
import javax.inject.Inject

class SearchRepository @Inject constructor(
    private val api: SearchService
) {
    suspend fun getMoviesFromQuery(query: String): List<MovieModel> {
        return api.getMoviesFromQuery(query).map {
            it.toMovieModel(listOf())
        }
    }

}