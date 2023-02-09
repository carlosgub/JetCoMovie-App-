package com.example.moviejetpackcompose.data.network.response

data class NowPlayingResponse(
    val page: Int,
    val results: List<MovieResponse>
)
