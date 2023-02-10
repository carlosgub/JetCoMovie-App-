package com.example.moviejetpackcompose.ui.features.movie

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.moviejetpackcompose.core.sealed.GenericState
import com.example.moviejetpackcompose.model.usecase.GetNowPlayingMoviesUseCase
import com.example.moviejetpackcompose.rule.TestCoroutineRule
import com.example.moviejetpackcompose.ui.features.model.MovieModel
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class MovieViewModelTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    internal val testCoroutineRule = TestCoroutineRule()

    @MockK
    lateinit var getNowPlayingMoviesUseCase: GetNowPlayingMoviesUseCase

    private lateinit var viewModel: MovieViewModel

    @Before
    fun setup() {
        MockKAnnotations.init()
        viewModel = MovieViewModel(getNowPlayingMoviesUseCase)
    }


    @Test
    fun `get recent movies successfully`() = runTest {
        val list = listOf(
            MovieModel(
                id = 1,
                originalTitle = "test",
                posterPath = "posterPath",
                releaseDate = "releaseDate",
                voteAverage = 20.0,
                voteCount = 1000,
                categories = listOf(),
                runtime = "runtime",
                overview = "overview"
            )
        )

        every {
            getNowPlayingMoviesUseCase()
        }.returns(
            flow {
                emit(list)
            }
        )

        val states = mutableListOf<GenericState<List<MovieModel>>>()

        viewModel.uiState
            .take(2)
            .toList(states)

        Assert.assertEquals(2, states.size)
        Assert.assertEquals(listOf(GenericState.Loading, GenericState.Success(list)), states)
    }

    @Test
    fun `get recent movies empty list`() = runTest {
        val list = listOf<MovieModel>()

        every {
            getNowPlayingMoviesUseCase()
        }.returns(
            flow {
                emit(list)
            }
        )

        val states = mutableListOf<GenericState<List<MovieModel>>>()

        viewModel.uiState
            .take(2)
            .toList(states)

        Assert.assertEquals(2, states.size)
        Assert.assertEquals(listOf(GenericState.Loading, GenericState.Success(list)), states)
    }

    @Test
    fun `get recent movies error`() = runTest {
        val message = "Error"

        every {
            getNowPlayingMoviesUseCase()
        }.throws(
            Exception(message)
        )

        val states = mutableListOf<GenericState<List<MovieModel>>>()

        viewModel.uiState
            .take(2)
            .toList(states)

        Assert.assertEquals(2, states.size)
        Assert.assertEquals(listOf(GenericState.Loading, GenericState.Error(message)), states)
    }
}
