package com.example.moviejetpackcompose.helpers

import com.example.moviejetpackcompose.features.movie.data.database.model.CategoryEntity
import com.example.moviejetpackcompose.features.movie.data.network.response.CategoriesResponse
import com.example.moviejetpackcompose.features.movie.data.network.response.MovieResponse
import com.example.moviejetpackcompose.features.movie.ui.model.MovieModel


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
        backdropPath = backdropPath,
        popularity = popularity,
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
        backdropPath = backdropPath,
        popularity = popularity,
        releaseDate = releaseDate,
        voteAverage = voteAverage,
        voteCount = voteCount,
        categories = categoriesList
    )
}