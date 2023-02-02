package com.example.moviejetpackcompose.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.moviejetpackcompose.features.detail.data.database.MovieDetailDao
import com.example.moviejetpackcompose.features.detail.data.database.model.MovieEntity
import com.example.moviejetpackcompose.features.movie.data.database.CategoryDao
import com.example.moviejetpackcompose.features.movie.data.database.model.CategoryEntity

@Database(entities = [CategoryEntity::class, MovieEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao

    abstract fun movieDetailDao(): MovieDetailDao
}