package com.example.moviejetpackcompose.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.moviejetpackcompose.data.database.dao.CategoryDao
import com.example.moviejetpackcompose.data.database.dao.MovieDetailDao
import com.example.moviejetpackcompose.data.database.dao.TicketDao
import com.example.moviejetpackcompose.data.database.model.CategoryEntity
import com.example.moviejetpackcompose.data.database.model.MovieEntity

@Database(entities = [CategoryEntity::class, MovieEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao

    abstract fun movieDetailDao(): MovieDetailDao

    abstract fun ticketDao(): TicketDao
}
