package com.example.moviejetpackcompose.data

import com.example.moviejetpackcompose.ui.features.model.MovieModel
import com.example.moviejetpackcompose.data.database.dao.TicketDao
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