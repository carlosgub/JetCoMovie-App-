package com.example.moviejetpackcompose.features.detail.data.database

import androidx.room.*
import com.example.moviejetpackcompose.features.detail.data.database.model.MovieEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDetailDao {

    @Query("SELECT * FROM MovieEntity WHERE id = :id")
    fun getMovie(id: String): Flow<MovieEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMovie(movieEntity: MovieEntity)

    @Delete
    suspend fun deleteMovie(movieEntity: MovieEntity)
}