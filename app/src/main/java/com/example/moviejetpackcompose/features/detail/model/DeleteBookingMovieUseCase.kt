package com.example.moviejetpackcompose.features.detail.model

import com.example.moviejetpackcompose.features.detail.data.DetailRepository
import com.example.moviejetpackcompose.features.movie.ui.model.MovieModel
import javax.inject.Inject

class DeleteBookingMovieUseCase @Inject constructor(
    private val detailRepository: DetailRepository
) {
    suspend operator fun invoke(movieModel: MovieModel): Unit =
        detailRepository.deleteMovie(movieModel)
}