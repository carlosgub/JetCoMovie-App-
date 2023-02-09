package com.example.moviejetpackcompose.ui.features.model

data class MovieModel(
    val id: Int,
    val originalTitle: String,
    val posterPath: String?,
    val releaseDate: String?,
    val voteAverage: Double?,
    val voteCount: Int,
    val categories: List<String>,
    val runtime: String? = null,
    val overview: String? = null
) {
    fun getImagePath(): String =
        if (posterPath != null) {
            "https://image.tmdb.org/t/p/w500$posterPath"
        } else {
            "https://i.stack.imgur.com/GNhx0.png"
        }
}
