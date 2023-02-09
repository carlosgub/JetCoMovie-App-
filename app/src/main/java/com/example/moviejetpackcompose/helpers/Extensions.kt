package com.example.moviejetpackcompose.helpers

import com.example.moviejetpackcompose.data.database.model.CategoryEntity
import com.example.moviejetpackcompose.data.database.model.MovieEntity
import com.example.moviejetpackcompose.data.network.response.CategoriesResponse
import com.example.moviejetpackcompose.data.network.response.MovieDetailResponse
import com.example.moviejetpackcompose.data.network.response.MovieResponse
import com.example.moviejetpackcompose.ui.features.model.MovieModel

fun MovieResponse.toMovieModel(categories: List<CategoryEntity>): MovieModel {
    val categoryMap = categories.associate {
        it.id to it.name
    }
    val categoriesList = this.genreIds.map {
        categoryMap[it].orEmpty()
    }
    return MovieModel(
        id = id,
        originalTitle = originalTitle,
        posterPath = posterPath,
        releaseDate = releaseDate,
        voteAverage = voteAverage,
        voteCount = voteCount,
        categories = categoriesList
    )
}

fun MovieResponse.toMovieModelWithCategoriesResponse(categories: List<CategoriesResponse>): MovieModel {
    val categoryMap = categories.associate {
        it.id to it.name
    }
    val categoriesList = this.genreIds.map {
        categoryMap[it].orEmpty()
    }
    return MovieModel(
        id = id,
        originalTitle = originalTitle,
        posterPath = posterPath,
        releaseDate = releaseDate,
        voteAverage = voteAverage,
        voteCount = voteCount,
        categories = categoriesList
    )
}

fun MovieDetailResponse.toMovieModel(): MovieModel =
    MovieModel(
        id = id,
        originalTitle = originalTitle,
        posterPath = posterPath,
        releaseDate = releaseDate,
        voteAverage = voteAverage,
        voteCount = voteCount,
        categories = genres.map {
            it.name.orEmpty()
        },
        runtime = runtime.minutesToTimeString(),
        overview = overview
    )

fun MovieModel.toMovieEntity(): MovieEntity =
    MovieEntity(
        id = id,
        originalTitle = originalTitle,
        posterPath = posterPath,
        releaseDate = releaseDate,
        voteAverage = voteAverage,
        voteCount = voteCount,
        categories = categories,
        runtime = runtime,
        overview = overview
    )

fun MovieEntity.toMovieModel(): MovieModel =
    MovieModel(
        id = id,
        originalTitle = originalTitle,
        posterPath = posterPath,
        releaseDate = releaseDate,
        voteAverage = voteAverage,
        voteCount = voteCount,
        categories = categories,
        runtime = runtime,
        overview = overview
    )

fun Int?.minutesToTimeString(): String? =
    if (this == null) {
        null
    } else {
        val hours: Int = this / 60
        val minutes: Int = this % 60
        "${hours}h ${minutes}m"
    }
