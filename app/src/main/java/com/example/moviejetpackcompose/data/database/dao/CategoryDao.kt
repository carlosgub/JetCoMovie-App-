package com.example.moviejetpackcompose.data.database.dao

import androidx.room.*
import com.example.moviejetpackcompose.data.database.model.CategoryEntity

@Dao
interface CategoryDao {

    @Query("SELECT * FROM CategoryEntity")
    fun getCategories(): List<CategoryEntity>

    @Insert
    suspend fun addCategories(item: CategoryEntity)
}