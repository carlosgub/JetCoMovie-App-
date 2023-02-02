package com.example.moviejetpackcompose.features.detail.data

import com.example.moviejetpackcompose.features.detail.data.network.DetailService
import com.example.moviejetpackcompose.features.movie.ui.model.MovieModel
import com.example.moviejetpackcompose.helpers.toMovieModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DetailRepository @Inject constructor(
    private val api: DetailService,
) {
    suspend fun getMovieDetail(movieId: String): MovieModel {
        return withContext(Dispatchers.Default) {
            api.getMovieDetail(movieId).toMovieModel()
        }
    }

}