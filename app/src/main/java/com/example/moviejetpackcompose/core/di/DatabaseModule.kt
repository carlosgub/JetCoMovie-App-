package com.example.moviejetpackcompose.core.di

import android.content.Context
import androidx.room.Room
import com.example.moviejetpackcompose.core.database.MovieDatabase
import com.example.moviejetpackcompose.data.database.dao.CategoryDao
import com.example.moviejetpackcompose.data.database.dao.MovieDetailDao
import com.example.moviejetpackcompose.data.database.dao.TicketDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    fun provideCategoryDao(movieDatabase: MovieDatabase): CategoryDao =
        movieDatabase.categoryDao()

    @Provides
    fun provideMovieDetailDao(movieDatabase: MovieDatabase): MovieDetailDao =
        movieDatabase.movieDetailDao()

    @Provides
    fun provideTicketDao(movieDatabase: MovieDatabase): TicketDao =
        movieDatabase.ticketDao()

    @Provides
    @Singleton
    fun provideTodoDatabase(@ApplicationContext appContext: Context): MovieDatabase =
        Room.databaseBuilder(appContext, MovieDatabase::class.java, "CATEGORY").build()
}
