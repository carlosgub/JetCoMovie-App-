package com.example.moviejetpackcompose.ui.features.ticket

import app.cash.turbine.test
import com.example.moviejetpackcompose.core.DispatcherProvider
import com.example.moviejetpackcompose.core.sealed.GenericState
import com.example.moviejetpackcompose.helpers.TestDispatcherProvider
import com.example.moviejetpackcompose.helpers.movieModel
import com.example.moviejetpackcompose.model.usecase.GetMoviesBookedUseCase
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
class TicketViewModelTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    private val dispatcherProvider: DispatcherProvider = TestDispatcherProvider()

    @MockK
    lateinit var getMoviesBookedUseCase: GetMoviesBookedUseCase

    private val message = "Error"

    @Test
    fun `get tickets successfully`() = runTest {
        val list = listOf(movieModel)

        every {
            getMoviesBookedUseCase.invoke()
        }.returns(
            flowOf(list)
        )
        val viewModel =
            TicketViewModel(
                getMoviesBookedUseCase,
                dispatcherProvider
            )
        viewModel.uiState.test {
            assertEquals(GenericState.Success(list), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
        verify { getMoviesBookedUseCase.invoke() }
    }

    @Test
    fun `get tickets empty successfully`() = runTest {
        val list = listOf<MovieModel>()

        every {
            getMoviesBookedUseCase.invoke()
        }.returns(
            flowOf(list)
        )
        val viewModel =
            TicketViewModel(
                getMoviesBookedUseCase,
                dispatcherProvider
            )
        viewModel.uiState.test {
            assertEquals(GenericState.Success(list), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
        verify { getMoviesBookedUseCase.invoke() }
    }

    @Test
    fun `get tickets error`() = runTest {
        every {
            getMoviesBookedUseCase.invoke()
        }.returns(
            flow {
                throw IllegalStateException(message)
            }
        )
        val viewModel =
            TicketViewModel(
                getMoviesBookedUseCase,
                dispatcherProvider
            )
        viewModel.uiState.test {
            assertEquals(GenericState.Error(message), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
        verify {
            getMoviesBookedUseCase.invoke()
        }
    }
}
