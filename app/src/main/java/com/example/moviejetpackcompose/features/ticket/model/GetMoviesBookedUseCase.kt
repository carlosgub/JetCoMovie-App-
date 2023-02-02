package com.example.moviejetpackcompose.features.ticket.model

import com.example.moviejetpackcompose.features.movie.ui.model.MovieModel
import com.example.moviejetpackcompose.features.ticket.data.TicketRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMoviesBookedUseCase @Inject constructor(
    private val ticketRepository: TicketRepository
) {
    operator fun invoke(): Flow<List<MovieModel>> =
        ticketRepository.getMoviesBooked()
}