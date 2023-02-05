@file:OptIn(ExperimentalCoroutinesApi::class)

package com.example.moviejetpackcompose.features.movie.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.moviejetpackcompose.usecase.GetNowPlayingMoviesUseCase
import com.example.moviejetpackcompose.ui.features.model.MovieModel
import com.example.moviejetpackcompose.ui.features.movie.MovieViewModel
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class MovieViewModelTest {

    @MockK
    lateinit var getNowPlayingMoviesUseCase: GetNowPlayingMoviesUseCase


    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: MovieViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(UnconfinedTestDispatcher())
        viewModel = MovieViewModel(getNowPlayingMoviesUseCase)
    }

    @After
    fun cleanUp() {
        Dispatchers.resetMain()
    }

    @Test
    fun test_getNowPlayingMoviesSuccess(): Unit = runTest {

        every {
            getNowPlayingMoviesUseCase.getMovieList()
        } answers {
            flow {
                emit(listOf())
            }
        }

        viewModel.uiState.collect {
            when (it) {
                is MovieUiState.Error -> assert(false)
                MovieUiState.Loading -> Unit
                is MovieUiState.Success -> assertEquals(listOf<MovieModel>(), it)
            }
        }
    }
}