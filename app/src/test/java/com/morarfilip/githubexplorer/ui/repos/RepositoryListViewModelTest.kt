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
    private lateinit var viewModel: RepoListViewModel

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
        viewModel = RepoListViewModel(getRepositoriesUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is Loading`() = runTest {
        assertEquals(RepoUiState.Loading, viewModel.uiState.value)
    }

    @Test
    fun `search query change updates state to Success after debounce`() = runTest {
        // Arrange
        val mockRepos = listOf(mockRepo)
        coEvery { getRepositoriesUseCase("kotlin") } returns Result.success(mockRepos)

        // Act & Assert
        viewModel.uiState.test {
            // 1. Initial emission
            assertEquals(RepoUiState.Loading, awaitItem())

            // 2. Trigger search
            viewModel.onSearchQueryChanged("kotlin")

            // 3. Advance time to skip the 1000ms debounce
            testScheduler.advanceTimeBy(1001)

            // 4. Check Success state
            val result = awaitItem()
            assertTrue(result is RepoUiState.Success)
            assertEquals(mockRepos, (result as RepoUiState.Success).repos)
        }
    }

    @Test
    fun `failed use case returns Error state`() = runTest {
        // Arrange
        val errorMessage = "API Rate Limit Exceeded"
        coEvery { getRepositoriesUseCase(any()) } returns Result.failure(Exception(errorMessage))

        // Act & Assert
        viewModel.uiState.test {
            assertEquals(RepoUiState.Loading, awaitItem())

            viewModel.onSearchQueryChanged("rust")
            testScheduler.advanceTimeBy(501)

            val result = awaitItem()
            assertTrue(result is RepoUiState.Error)
            assertEquals(errorMessage, (result as RepoUiState.Error).message)
        }
    }

    @Test
    fun `empty result returns Success with empty list`() = runTest {
        // Arrange
        coEvery { getRepositoriesUseCase("nonexistent") } returns Result.success(emptyList())

        // Act & Assert
        viewModel.uiState.test {
            assertEquals(RepoUiState.Loading, awaitItem())

            viewModel.onSearchQueryChanged("nonexistent")
            testScheduler.advanceTimeBy(501)

            val result = awaitItem()
            assertTrue(result is RepoUiState.Success)
            assertTrue((result as RepoUiState.Success).repos.isEmpty())
        }
    }

    @Test
    fun `refresh triggers reload of current query`() = runTest {
        // Arrange
        val query = "kotlin"
        val mockRepos = listOf(mockRepo)
        coEvery { getRepositoriesUseCase(query) } returns Result.success(mockRepos)

        viewModel.uiState.test {
            assertEquals(RepoUiState.Loading, awaitItem())

            viewModel.onSearchQueryChanged(query)
            testScheduler.advanceTimeBy(1001) // Debounce
            testScheduler.advanceTimeBy(501)  // flatMapLatest delay

            assertEquals(RepoUiState.Success(mockRepos), awaitItem())

            // Act
            viewModel.refresh()

            // Assert
            assertEquals(RepoUiState.Loading, awaitItem())

            testScheduler.advanceTimeBy(501)

            val result = awaitItem()
            assertTrue(result is RepoUiState.Success)
            assertEquals(mockRepos, (result as RepoUiState.Success).repos)
        }
    }
}