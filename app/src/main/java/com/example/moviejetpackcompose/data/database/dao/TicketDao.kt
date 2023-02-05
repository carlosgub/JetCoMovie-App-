package com.example.moviejetpackcompose.data.database.dao

import androidx.room.*
import com.example.moviejetpackcompose.data.database.model.MovieEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TicketDao {

    @Query("SELECT * FROM MovieEntity")
    fun getMovies(): Flow<List<MovieEntity>>
}