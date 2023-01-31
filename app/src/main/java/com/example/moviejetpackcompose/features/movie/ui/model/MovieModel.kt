package com.example.moviejetpackcompose.features.movie.ui.model

class MovieModel(
    val id: Int,
    val originalTitle: String,
    val posterPath: String?,
    val backdropPath: String?,
    val popularity: Double?,
    val releaseDate: String?,
    val voteAverage: Double?,
    val voteCount: Int,
    val categories: List<String>
) {
    fun getImagePath(): String {
        return if (posterPath != null) {
            "https://image.tmdb.org/t/p/w500${posterPath}"
        } else {
            "https://i.stack.imgur.com/GNhx0.png"
        }
    }

    fun getBackdropImagePath(): String {
        return if (backdropPath != null) {
            "https://image.tmdb.org/t/p/w500${backdropPath}"
        } else {
            "https://i.stack.imgur.com/GNhx0.png"
        }
    }
}