package com.example.moviejetpackcompose.features.movie.data.network

import com.example.moviejetpackcompose.BuildConfig
import com.example.moviejetpackcompose.features.movie.data.network.response.CategoriesResponse
import com.example.moviejetpackcompose.features.movie.data.network.response.GetCategoriesResponse
import com.example.moviejetpackcompose.features.movie.data.network.response.MovieResponse
import com.example.moviejetpackcompose.features.movie.data.network.response.NowPlayingResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieClient {

    @GET("/3/movie/now_playing")
    suspend fun getNowPlayingMovies(
        @Query("api_key") apiKey: String = BuildConfig.API_KEY,
        @Query("language") language: String = "en-ES",
        @Query("page") page: Int = 1
    ): Response<NowPlayingResponse>

    @GET("/3/genre/movie/list")
    suspend fun getCategories(
        @Query("api_key") apiKey: String = BuildConfig.API_KEY,
        @Query("language") language: String = "en-ES"
    ): Response<GetCategoriesResponse>
}