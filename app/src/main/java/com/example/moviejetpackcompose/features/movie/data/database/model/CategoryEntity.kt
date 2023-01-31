package com.example.moviejetpackcompose.features.movie.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CategoryEntity(
    @PrimaryKey
    val id: Int,
    val name: String
)