package com.example.moviejetpackcompose.features.search.data.network

import com.example.moviejetpackcompose.BuildConfig
import com.example.moviejetpackcompose.features.movie.data.network.response.GetCategoriesResponse
import com.example.moviejetpackcompose.features.movie.data.network.response.NowPlayingResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchClient {

    @GET("/3/search/movie")
    suspend fun getMoviesFromQuery(
        @Query("api_key") apiKey: String = BuildConfig.API_KEY,
        @Query("page") page: Int = 1,
        @Query("query") query: String
    ): Response<NowPlayingResponse>
}