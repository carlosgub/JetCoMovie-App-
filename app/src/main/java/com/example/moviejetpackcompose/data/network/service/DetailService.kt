package com.example.moviejetpackcompose.data.network.service

import com.example.moviejetpackcompose.data.network.clients.DetailClient
import com.example.moviejetpackcompose.data.network.response.MovieDetailResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DetailService @Inject constructor(
    private val detailClient: DetailClient
) {
    suspend fun getMovieDetail(movieId: String): MovieDetailResponse =
        withContext(Dispatchers.IO) {
            val response = detailClient.getMovieDetail(movieId = movieId)
            response.body()!!
        }
}
