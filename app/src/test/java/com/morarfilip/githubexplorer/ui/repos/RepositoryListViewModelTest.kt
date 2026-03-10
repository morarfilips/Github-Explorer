package com.morarfilip.githubexplorer.ui.repos

import app.cash.turbine.test
import com.morarfilip.githubexplorer.core.domain.usecase.GetRepositoriesUseCase
import com.morarfilip.githubexplorer.core.model.Repository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RepositoryListViewModelTest {

    private val getRepositoriesUseCase: GetRepositoriesUseCase = mockk()
    private lateinit var viewModel: RepositoryListViewModel

    private val testDispatcher = StandardTestDispatcher()

    private val mockRepo = Repository(
        1L,
        "Kotlin-Repo",
        "Desc",
        10,
        5,
        8,
        2,
        "2026-03-09T15:54:11Z",
        "jetbrains",
        "url",
        "Kotlin",
        "Apache 2.0"
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = RepositoryListViewModel(getRepositoriesUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is Loading`() = runTest {
        assertEquals(RepositoryUiState.Loading, viewModel.uiState.value)
    }

    @Test
    fun `search query change updates state to Success after debounce and delay`() = runTest {
        // Arrange
        val mockRepos = listOf(mockRepo)
        coEvery { getRepositoriesUseCase("kotlin") } returns Result.success(mockRepos)

        // Act & Assert
        viewModel.uiState.test {
            assertEquals(RepositoryUiState.Loading, awaitItem())

            viewModel.onIntent(RepositoryListIntent.SearchQueryChanged("kotlin"))

            testScheduler.advanceTimeBy(1001)
            testScheduler.advanceTimeBy(501)

            val result = awaitItem()
            assertTrue(result is RepositoryUiState.Success)
            assertEquals(mockRepos, (result as RepositoryUiState.Success).repos)
        }
    }

    @Test
    fun `failed use case returns Error state`() = runTest {
        // Arrange
        val errorMessage = "API Rate Limit Exceeded"
        coEvery { getRepositoriesUseCase(any()) } returns Result.failure(Exception(errorMessage))

        // Act & Assert
        viewModel.uiState.test {
            assertEquals(RepositoryUiState.Loading, awaitItem())

            viewModel.onIntent(RepositoryListIntent.SearchQueryChanged("rust"))
            testScheduler.advanceTimeBy(1001)
            testScheduler.advanceTimeBy(501)

            val result = awaitItem()
            assertTrue(result is RepositoryUiState.Error)
            assertEquals(errorMessage, (result as RepositoryUiState.Error).message)
        }
    }

    @Test
    fun `empty result returns Success with empty list`() = runTest {
        // Arrange
        coEvery { getRepositoriesUseCase("nonexistent") } returns Result.success(emptyList())

        // Act & Assert
        viewModel.uiState.test {
            assertEquals(RepositoryUiState.Loading, awaitItem())

            viewModel.onIntent(RepositoryListIntent.SearchQueryChanged("nonexistent"))
            testScheduler.advanceTimeBy(1001)
            testScheduler.advanceTimeBy(501)

            val result = awaitItem()
            assertTrue(result is RepositoryUiState.Success)
            assertTrue((result as RepositoryUiState.Success).repos.isEmpty())
        }
    }

    @Test
    fun `refresh triggers reload of current query`() = runTest {
        // Arrange
        val query = "kotlin"
        val mockRepos = listOf(mockRepo)
        coEvery { getRepositoriesUseCase(query) } returns Result.success(mockRepos)

        // Act & Assert
        viewModel.uiState.test {
            assertEquals(RepositoryUiState.Loading, awaitItem())

            viewModel.onIntent(RepositoryListIntent.SearchQueryChanged(query))
            testScheduler.advanceTimeBy(1001) // Debounce
            testScheduler.advanceTimeBy(501)  // flatMapLatest delay

            assertEquals(RepositoryUiState.Success(mockRepos), awaitItem())

            viewModel.onIntent(RepositoryListIntent.RefreshTriggered)

            assertEquals(RepositoryUiState.Loading, awaitItem())

            testScheduler.advanceTimeBy(501)

            val result = awaitItem()
            assertTrue(result is RepositoryUiState.Success)
            assertEquals(mockRepos, (result as RepositoryUiState.Success).repos)
        }
    }
}