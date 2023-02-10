package com.example.moviejetpackcompose.ui.features.movie

import com.example.moviejetpackcompose.core.sealed.GenericState
import com.example.moviejetpackcompose.model.usecase.GetNowPlayingMoviesUseCase
import com.example.moviejetpackcompose.ui.features.model.MovieModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class MovieViewModelTest {

    @Mock
    lateinit var getNowPlayingMoviesUseCase: GetNowPlayingMoviesUseCase

    private lateinit var viewModel: MovieViewModel

    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        viewModel = MovieViewModel(getNowPlayingMoviesUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
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

        whenever(getNowPlayingMoviesUseCase()).thenReturn(
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

        whenever(getNowPlayingMoviesUseCase()).thenReturn(
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

        whenever(getNowPlayingMoviesUseCase()).thenReturn(
            flow {
                throw Exception(message)
            }
        )

        val states = mutableListOf<GenericState<List<MovieModel>>>()

        viewModel.uiState
            .take(2)
            .toList(states)

        Assert.assertEquals(2, states.size)
        Assert.assertEquals(listOf(GenericState.Loading, GenericState.Error(message)), states)
    }
}
