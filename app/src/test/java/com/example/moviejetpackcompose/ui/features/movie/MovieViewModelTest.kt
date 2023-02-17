package com.example.moviejetpackcompose.ui.features.movie

import app.cash.turbine.test
import com.example.moviejetpackcompose.core.DispatcherProvider
import com.example.moviejetpackcompose.core.sealed.GenericState
import com.example.moviejetpackcompose.helpers.TestDispatcherProvider
import com.example.moviejetpackcompose.helpers.movieModel
import com.example.moviejetpackcompose.model.usecase.GetNowPlayingMoviesUseCase
import com.example.moviejetpackcompose.ui.features.model.MovieModel
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class MovieViewModelTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    private val dispatcherProvider: DispatcherProvider = TestDispatcherProvider()

    @MockK
    lateinit var getNowPlayingMoviesUseCase: GetNowPlayingMoviesUseCase

    @Test
    fun `get recent movies successfully`() = runTest {
        val list = listOf(
            movieModel
        )

        every {
            getNowPlayingMoviesUseCase.invoke()
        }.returns(
            flowOf(list)
        )
        val viewModel =
            MovieViewModel(getNowPlayingMoviesUseCase, dispatcherProvider)
        viewModel.uiState.test {
            assertEquals(GenericState.Success(list), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
        verify { getNowPlayingMoviesUseCase.invoke() }
    }

    @Test
    fun `get recent movies empty list`() = runTest {
        val list = listOf<MovieModel>()

        every {
            getNowPlayingMoviesUseCase.invoke()
        }.returns(
            flowOf(list)
        )
        val viewModel =
            MovieViewModel(getNowPlayingMoviesUseCase, dispatcherProvider)
        viewModel.uiState.test {
            assertEquals(GenericState.Success(list), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
        verify { getNowPlayingMoviesUseCase.invoke() }
    }

    @Test
    fun `get recent movies error`() = runTest {
        val message = "Error"
        every {
            getNowPlayingMoviesUseCase.invoke()
        }.returns(
            flow {
                throw IllegalStateException(message)
            }
        )
        val viewModel =
            MovieViewModel(getNowPlayingMoviesUseCase, dispatcherProvider)
        viewModel.uiState.test {
            assertEquals(GenericState.Error(message), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
        verify { getNowPlayingMoviesUseCase.invoke() }
    }
}
