package com.example.moviejetpackcompose.features.detail.data.network

import com.example.moviejetpackcompose.features.detail.data.network.response.MovieDetailResponse
import com.example.moviejetpackcompose.features.movie.data.network.response.MovieResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DetailService @Inject constructor(
    private val detailClient: DetailClient
) {
    suspend fun getMovieDetail(movieId: String): MovieDetailResponse {
        return withContext(Dispatchers.IO) {
            val response = detailClient.getMovieDetail(movieId = movieId)
            response.body()!!
        }
    }
}