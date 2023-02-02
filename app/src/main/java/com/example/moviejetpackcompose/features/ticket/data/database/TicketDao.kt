package com.example.moviejetpackcompose.features.ticket.data.database

import androidx.room.*
import com.example.moviejetpackcompose.features.detail.data.database.model.MovieEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TicketDao {

    @Query("SELECT * FROM MovieEntity")
    fun getMovies(): Flow<List<MovieEntity>>
}