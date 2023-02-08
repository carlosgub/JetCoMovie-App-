package com.example.moviejetpackcompose.model.usecase

import com.example.moviejetpackcompose.data.DetailRepository
import com.example.moviejetpackcompose.ui.features.model.MovieModel
import javax.inject.Inject

class DeleteBookingMovieUseCase @Inject constructor(
    private val detailRepository: DetailRepository
) {
    suspend operator fun invoke(movieModel: MovieModel) =
        detailRepository.deleteMovie(movieModel)
}
