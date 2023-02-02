package com.example.moviejetpackcompose.features.movie.data.database

import androidx.room.*
import com.example.moviejetpackcompose.features.movie.data.database.model.CategoryEntity

@Dao
interface CategoryDao {

    @Query("SELECT * FROM CategoryEntity")
    fun getCategories(): List<CategoryEntity>

    @Insert
    suspend fun addCategories(item: CategoryEntity)
}