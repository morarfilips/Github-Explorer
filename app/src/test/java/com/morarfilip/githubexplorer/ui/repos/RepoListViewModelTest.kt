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
class RepoListViewModelTest {

    private val getRepositoriesUseCase: GetRepositoriesUseCase = mockk()
    private lateinit var viewModel: RepoListViewModel

    private val testDispatcher = StandardTestDispatcher()

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
        val mockRepos = listOf(
            Repository(
                1,
                "Kotlin-Repo",
                "Desc",
                10,
                5,
                "2026",
                "url",
                "Kotlin"
            )
        )
        coEvery { getRepositoriesUseCase("kotlin") } returns Result.success(mockRepos)

        // Act & Assert
        viewModel.uiState.test {
            // 1. Initial emission
            assertEquals(RepoUiState.Loading, awaitItem())

            viewModel.onSearchQueryChanged("kotlin")

            // 2. Advance time by 500ms to trigger debounce
            testScheduler.advanceTimeBy(501)

            // 3. Check Success state
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
}