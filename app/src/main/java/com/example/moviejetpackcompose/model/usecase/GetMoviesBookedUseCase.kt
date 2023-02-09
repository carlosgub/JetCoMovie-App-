package com.example.moviejetpackcompose.model.usecase

import com.example.moviejetpackcompose.data.TicketRepository
import com.example.moviejetpackcompose.ui.features.model.MovieModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMoviesBookedUseCase @Inject constructor(
    private val ticketRepository: TicketRepository
) {
    operator fun invoke(): Flow<List<MovieModel>> =
        ticketRepository.getMoviesBooked()
}
