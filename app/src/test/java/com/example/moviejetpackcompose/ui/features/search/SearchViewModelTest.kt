package com.example.moviejetpackcompose.ui.features.search

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.example.moviejetpackcompose.core.DispatcherProvider
import com.example.moviejetpackcompose.core.sealed.GenericState
import com.example.moviejetpackcompose.helpers.TestDispatcherProvider
import com.example.moviejetpackcompose.model.usecase.GetMoviesFromQueryUseCase
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import rule.MainCoroutineRule

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class SearchViewModelTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    private val dispatcherProvider: DispatcherProvider = TestDispatcherProvider()

    @MockK
    lateinit var getMoviesFromQueryUseCase: GetMoviesFromQueryUseCase

    private val queryShorter = "ga"
    private val queryLarge = "gato"

    @Test
    fun `verify that query changes and not show none`() = runTest {
        val viewModel =
            SearchViewModel(getMoviesFromQueryUseCase, dispatcherProvider)
        viewModel.queryFieldChange(queryShorter)
        viewModel.uiState.test {
            assertEquals(GenericState.None, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
        assertEquals(queryShorter, viewModel.query.value)
    }

    @Test
    fun `verify that query changes and show loading`() = runTest {
        val viewModel =
            SearchViewModel(getMoviesFromQueryUseCase, dispatcherProvider)
        viewModel.queryFieldChange(queryLarge)
        viewModel.uiState.test {
            assertEquals(GenericState.Loading, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
        assertEquals(queryLarge, viewModel.query.value)
    }
}
