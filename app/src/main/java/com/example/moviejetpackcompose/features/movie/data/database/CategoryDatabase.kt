package com.example.moviejetpackcompose.features.movie.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.moviejetpackcompose.features.movie.data.database.model.CategoryEntity

@Database(entities = [CategoryEntity::class], version = 1)
abstract class CategoryDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
}