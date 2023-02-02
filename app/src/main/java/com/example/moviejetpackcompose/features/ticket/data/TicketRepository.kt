package com.example.moviejetpackcompose.features.ticket.data

import com.example.moviejetpackcompose.features.movie.ui.model.MovieModel
import com.example.moviejetpackcompose.features.ticket.data.database.TicketDao
import com.example.moviejetpackcompose.helpers.toMovieModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TicketRepository @Inject constructor(
    private val ticketDao: TicketDao
) {

    fun getMoviesBooked(): Flow<List<MovieModel>> {
        return ticketDao.getMovies().map { list ->
            list.map { movieEntity ->
                movieEntity.toMovieModel()
            }
        }
    }
}