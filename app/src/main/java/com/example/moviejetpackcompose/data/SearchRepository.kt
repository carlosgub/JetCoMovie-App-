package com.example.moviejetpackcompose.data

import com.example.moviejetpackcompose.ui.features.model.MovieModel
import com.example.moviejetpackcompose.data.network.service.SearchService
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