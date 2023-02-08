package com.example.moviejetpackcompose.model.usecase

import com.example.moviejetpackcompose.data.DetailRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class IsMovieBookedUseCase @Inject constructor(
    private val detailRepository: DetailRepository
) {
    operator fun invoke(movieId: String): Flow<Boolean> =
        detailRepository.isMovieBooked(movieId)
}
