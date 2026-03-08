package com.morarfilip.githubexplorer.core.domain

import com.morarfilip.githubexplorer.core.data.repository.GithubRepository
import com.morarfilip.githubexplorer.core.domain.usecase.GetRepositoriesUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class GetRepositoriesUseCaseTest {
    private val repository: GithubRepository = mockk()
    private lateinit var useCase: GetRepositoriesUseCase

    @Before
    fun setup() {
        useCase = GetRepositoriesUseCase(repository)
    }

    @Test
    fun `invoke calls repository with correct parameters`() = runTest {
        // Arrange
        coEvery { repository.getRepositories("kotlin", "stars") } returns Result.success(emptyList())

        // Act
        useCase("kotlin", "stars")

        // Assert
        coVerify { repository.getRepositories("kotlin", "stars") }
    }
}