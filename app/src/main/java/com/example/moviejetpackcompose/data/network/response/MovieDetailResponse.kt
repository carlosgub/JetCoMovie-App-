package com.example.moviejetpackcompose.data.network.response

import com.google.gson.annotations.SerializedName
import kotlinx.collections.immutable.ImmutableList

data class MovieDetailResponse(
    val id: Int,
    @SerializedName("original_title") val originalTitle: String,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("backdrop_path") val backdropPath: String?,
    val popularity: Double?,
    @SerializedName("release_date") val releaseDate: String?,
    @SerializedName("vote_average") val voteAverage: Double?,
    @SerializedName("vote_count") val voteCount: Int,
    @SerializedName("genres") val genres: ImmutableList<CategoriesResponse>,
    @SerializedName("runtime") val runtime: Int?,
    @SerializedName("overview") val overview: String?
)
