package com.example.moviejetpackcompose.data.network.clients

import com.example.moviejetpackcompose.BuildConfig
import com.example.moviejetpackcompose.data.network.response.MovieDetailResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface DetailClient {
    @GET("/3/movie/{movie_id}")
    suspend fun getMovieDetail(
        @Path("movie_id") movieId: String,
        @Query("api_key") apiKey: String = BuildConfig.API_KEY
    ): Response<MovieDetailResponse>
}
