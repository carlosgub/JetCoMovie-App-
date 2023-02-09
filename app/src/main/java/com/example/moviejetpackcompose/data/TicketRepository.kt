package com.example.moviejetpackcompose.data

import com.example.moviejetpackcompose.data.database.dao.TicketDao
import com.example.moviejetpackcompose.helpers.toMovieModel
import com.example.moviejetpackcompose.ui.features.model.MovieModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TicketRepository @Inject constructor(
    private val ticketDao: TicketDao
) {
    fun getMoviesBooked(): Flow<List<MovieModel>> =
        ticketDao.getMovies().map { list ->
            list.map { movieEntity ->
                movieEntity.toMovieModel()
            }
        }
}
