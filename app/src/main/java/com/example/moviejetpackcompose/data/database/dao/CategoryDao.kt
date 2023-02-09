package com.example.moviejetpackcompose.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.moviejetpackcompose.data.database.model.CategoryEntity

@Dao
interface CategoryDao {

    @Query("SELECT * FROM CategoryEntity")
    fun getCategories(): List<CategoryEntity>

    @Insert
    suspend fun addCategories(item: CategoryEntity)
}
