package com.example.moviejetpackcompose.core.di

import android.content.Context
import androidx.room.Room
import com.example.moviejetpackcompose.features.movie.data.database.CategoryDao
import com.example.moviejetpackcompose.features.movie.data.database.CategoryDatabase
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
    fun provideTaskDao(categoryDatabase: CategoryDatabase): CategoryDao {
        return categoryDatabase.categoryDao()
    }

    @Provides
    @Singleton
    fun provideTodoDatabase(@ApplicationContext appContext: Context): CategoryDatabase {
        return Room.databaseBuilder(appContext, CategoryDatabase::class.java, "CATEGORY").build()
    }
}