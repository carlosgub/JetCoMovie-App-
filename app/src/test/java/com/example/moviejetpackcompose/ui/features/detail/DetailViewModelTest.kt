package com.example.moviejetpackcompose.ui.features.detail

import app.cash.turbine.test
import com.example.moviejetpackcompose.core.DispatcherProvider
import com.example.moviejetpackcompose.core.sealed.GenericState
import com.example.moviejetpackcompose.helpers.TestDispatcherProvider
import com.example.moviejetpackcompose.helpers.movieModel
import com.example.moviejetpackcompose.model.usecase.BookingMovieUseCase
import com.example.moviejetpackcompose.model.usecase.DeleteBookingMovieUseCase
import com.example.moviejetpackcompose.model.usecase.GetMovieDetailUseCase
import com.example.moviejetpackcompose.model.usecase.IsMovieBookedUseCase
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
class DetailViewModelTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    private val dispatcherProvider: DispatcherProvider = TestDispatcherProvider()

    @MockK
    lateinit var getMovieDetailUseCase: GetMovieDetailUseCase

    @MockK
    lateinit var isMovieBookedUseCase: IsMovieBookedUseCase

    @MockK
    lateinit var bookingMovieUseCase: BookingMovieUseCase

    @MockK
    lateinit var deleteBookingMovieUseCase: DeleteBookingMovieUseCase

    private val movieId = "123"
    private val message = "Error"

    @Test
    fun `get movie successfully`() = runTest {
        every {
            getMovieDetailUseCase.invoke(any())
        }.returns(
            flowOf(movieModel)
        )
        val viewModel =
            DetailViewModel(
                getMovieDetailUseCase,
                isMovieBookedUseCase,
                bookingMovieUseCase,
                deleteBookingMovieUseCase,
                dispatcherProvider
            )
        viewModel.getMovieDetail(movieId)
        viewModel.uiState.test {
            assertEquals(GenericState.Success(movieModel), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
        verify { getMovieDetailUseCase.invoke(any()) }
    }

    @Test
    fun `get movie error`() = runTest {
        every {
            getMovieDetailUseCase.invoke(any())
        }.returns(
            flow {
                throw IllegalStateException(message)
            }
        )
        val viewModel =
            DetailViewModel(
                getMovieDetailUseCase,
                isMovieBookedUseCase,
                bookingMovieUseCase,
                deleteBookingMovieUseCase,
                dispatcherProvider
            )
        viewModel.getMovieDetail(movieId)
        viewModel.uiState.test {
            assertEquals(GenericState.Error(message), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
        verify {
            getMovieDetailUseCase.invoke(any())
        }
    }

    @Test
    fun `get movie saved successfully`() = runTest {
        every {
            isMovieBookedUseCase.invoke(any())
        }.returns(
            flowOf(true)
        )
        val viewModel =
            DetailViewModel(
                getMovieDetailUseCase,
                isMovieBookedUseCase,
                bookingMovieUseCase,
                deleteBookingMovieUseCase,
                dispatcherProvider
            )
        viewModel.isMovieBookedState(movieId)
        viewModel.bookingState.test {
            assertEquals(GenericState.Success(true), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
        verify { isMovieBookedUseCase.invoke(any()) }
    }

    @Test
    fun `get movie saved error`() = runTest {
        every {
            isMovieBookedUseCase.invoke(any())
        }.returns(
            flow {
                throw IllegalStateException(message)
            }
        )
        val viewModel =
            DetailViewModel(
                getMovieDetailUseCase,
                isMovieBookedUseCase,
                bookingMovieUseCase,
                deleteBookingMovieUseCase,
                dispatcherProvider
            )
        viewModel.isMovieBookedState(movieId)
        viewModel.bookingState.test {
            assertEquals(GenericState.Error(message), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
        verify {
            isMovieBookedUseCase.invoke(any())
        }
    }
}
