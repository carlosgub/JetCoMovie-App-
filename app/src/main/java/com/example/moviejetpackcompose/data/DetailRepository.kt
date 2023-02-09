package com.example.moviejetpackcompose.data

import com.example.moviejetpackcompose.data.database.dao.MovieDetailDao
import com.example.moviejetpackcompose.data.network.service.DetailService
import com.example.moviejetpackcompose.helpers.toMovieEntity
import com.example.moviejetpackcompose.helpers.toMovieModel
import com.example.moviejetpackcompose.ui.features.model.MovieModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DetailRepository @Inject constructor(
    private val api: DetailService,
    private val movieDetailDao: MovieDetailDao
) {
    suspend fun getMovieDetail(movieId: String): MovieModel =
        withContext(Dispatchers.Default) {
            api.getMovieDetail(movieId).toMovieModel()
        }

    fun isMovieBooked(id: String): Flow<Boolean> =
        movieDetailDao.getMovie(id).map {
            it != null
        }

    suspend fun addMovie(movieModel: MovieModel) {
        movieDetailDao.addMovie(movieModel.toMovieEntity())
    }

    suspend fun deleteMovie(movieModel: MovieModel) {
        movieDetailDao.deleteMovie(movieModel.toMovieEntity())
    }
}
