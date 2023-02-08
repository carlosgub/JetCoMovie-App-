package com.example.moviejetpackcompose.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MovieEntity(
    @PrimaryKey
    val id: Int,
    val originalTitle: String,
    val posterPath: String?,
    val releaseDate: String?,
    val voteAverage: Double?,
    val voteCount: Int,
    val categories: List<String>,
    val runtime: String? = null,
    val overview: String? = null
)
