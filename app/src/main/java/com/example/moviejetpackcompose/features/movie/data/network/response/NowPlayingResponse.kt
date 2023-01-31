package com.example.moviejetpackcompose.features.movie.data.network.response

data class NowPlayingResponse(
    val page: Int,
    val results:List<MovieResponse>
)