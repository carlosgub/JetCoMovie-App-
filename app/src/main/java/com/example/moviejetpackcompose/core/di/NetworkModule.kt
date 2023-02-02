package com.example.moviejetpackcompose.core.di

import com.example.moviejetpackcompose.features.detail.data.network.DetailClient
import com.example.moviejetpackcompose.features.movie.data.network.MovieClient
import com.example.moviejetpackcompose.features.search.data.network.SearchClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Singleton
    @Provides
    fun providesMovieClient(retrofit: Retrofit): MovieClient =
        retrofit.create(MovieClient::class.java)

    @Singleton
    @Provides
    fun providesDetailClient(retrofit: Retrofit): DetailClient =
        retrofit.create(DetailClient::class.java)

    @Singleton
    @Provides
    fun providesSearchClient(retrofit: Retrofit): SearchClient =
        retrofit.create(SearchClient::class.java)
}