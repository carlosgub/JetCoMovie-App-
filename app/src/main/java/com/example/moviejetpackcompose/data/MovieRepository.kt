package com.example.moviejetpackcompose.data

import com.example.moviejetpackcompose.data.database.dao.CategoryDao
import com.example.moviejetpackcompose.data.database.model.CategoryEntity
import com.example.moviejetpackcompose.data.network.service.MovieService
import com.example.moviejetpackcompose.helpers.toMovieModel
import com.example.moviejetpackcompose.helpers.toMovieModelWithCategoriesResponse
import com.example.moviejetpackcompose.ui.features.model.MovieModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MovieRepository @Inject constructor(
    private val api: MovieService,
    private val categoryDao: CategoryDao
) {
    suspend fun getNowPlayingMovies(): List<MovieModel> =
        withContext(Dispatchers.Default) {
            val listCategory = categoryDao.getCategories()
            if (listCategory.isEmpty()) {
                val categories = api.getCategories()
                categories.forEach {
                    categoryDao.addCategories(
                        CategoryEntity(
                            it.id!!,
                            it.name!!
                        )
                    )
                }
                api.getNowPlayingMovies().map {
                    async {
                        it.toMovieModelWithCategoriesResponse(categories)
                    }
                }.awaitAll()
            } else {
                api.getNowPlayingMovies().map {
                    async {
                        it.toMovieModel(listCategory)
                    }
                }.awaitAll()
            }
        }
}
