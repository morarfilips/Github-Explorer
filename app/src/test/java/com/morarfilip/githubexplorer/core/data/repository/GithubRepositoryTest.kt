package com.morarfilip.githubexplorer.core.data.repository

import com.morarfilip.githubexplorer.core.networking.GithubApiService
import com.morarfilip.githubexplorer.core.networking.dto.GithubSearchResponse
import com.morarfilip.githubexplorer.core.networking.dto.GithubRepoDto
import com.morarfilip.githubexplorer.core.networking.dto.OwnerDto
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class GithubRepositoryTest {

    private lateinit var repository: GithubRepositoryImpl
    private val apiService: GithubApiService = mockk()

    @Before
    fun setup() {
        repository = GithubRepositoryImpl(apiService)
    }

    @Test
    fun `getRepositories returns success when API call is successful`() = runTest {
        // Arrange
        val mockDto = GithubRepoDto(
            id = 1,
            name = "Test Repo",
            description = "Description",
            stars = 100,
            forks = 10,
            updatedAt = "2026-03-08",
            owner = OwnerDto("https://avatar.url"),
            language = "Kotlin"
        )
        val mockResponse = GithubSearchResponse(items = listOf(mockDto))

        coEvery { apiService.searchRepositories(any(), any(), any()) } returns mockResponse

        // Act
        val result = repository.getRepositories("kotlin")

        // Assert
        assert(result.isSuccess)
        val repos = result.getOrNull()
        assert(repos?.size == 1)
        assert(repos?.first()?.name == "Test Repo")
    }

    @Test
    fun `getRepositories returns failure when API call throws exception`() = runTest {
        // Arrange
        coEvery { apiService.searchRepositories(any(), any(), any()) } throws Exception("Network Error")

        // Act
        val result = repository.getRepositories("kotlin")

        // Assert
        assert(result.isFailure)
        assert(result.exceptionOrNull()?.message == "Network Error")
    }
}