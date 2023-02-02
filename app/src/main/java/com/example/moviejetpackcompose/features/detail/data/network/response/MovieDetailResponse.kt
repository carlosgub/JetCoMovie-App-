package com.example.moviejetpackcompose.features.detail.data.network.response

import com.example.moviejetpackcompose.features.movie.data.network.response.CategoriesResponse
import com.google.gson.annotations.SerializedName

data class MovieDetailResponse(
    val id: Int,
    @SerializedName("original_title") val originalTitle: String,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("backdrop_path") val backdropPath: String?,
    val popularity: Double?,
    @SerializedName("release_date") val releaseDate: String?,
    @SerializedName("vote_average") val voteAverage: Double?,
    @SerializedName("vote_count") val voteCount: Int,
    @SerializedName("genres") val genres: List<CategoriesResponse>,
    @SerializedName("runtime") val runtime: Int?,
    @SerializedName("overview") val overview: String?
)