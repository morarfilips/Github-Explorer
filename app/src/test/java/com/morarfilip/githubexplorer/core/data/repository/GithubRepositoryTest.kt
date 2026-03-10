package com.morarfilip.githubexplorer.core.data.repository

import com.morarfilip.githubexplorer.core.networking.GithubApiService
import com.morarfilip.githubexplorer.core.networking.dto.GithubSearchResponse
import com.morarfilip.githubexplorer.core.networking.dto.GithubRepositoryDto
import com.morarfilip.githubexplorer.core.networking.dto.LicenseDto
import com.morarfilip.githubexplorer.core.networking.dto.OwnerDto
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
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
        val mockDto = GithubRepositoryDto(
            id = 1,
            name = "Test Repo",
            description = "Description",
            stars = 100,
            forks = 10,
            watchers = 50,
            openIssues = 5,
            updatedAt = "2026-03-09T15:54:11Z",
            owner = OwnerDto(login = "kotlin_dev", avatarUrl = "https://avatar.url"),
            language = "Kotlin",
            license = LicenseDto(name = "Apache 2.0")
        )
        val mockResponse = GithubSearchResponse(items = listOf(mockDto))

        coEvery { apiService.searchRepositories(any(), any(), any()) } returns mockResponse

        // Act
        val result = repository.getRepositories("kotlin")

        // Assert
        assertTrue(result.isSuccess)
        val repos = result.getOrNull()

        assertEquals(1, repos?.size)
        val repo = repos?.first()
        assertEquals("Test Repo", repo?.name)
        assertEquals("kotlin_dev", repo?.ownerName)
        assertEquals(50, repo?.watchers)
        assertEquals(5, repo?.openIssues)
        assertEquals("Apache 2.0", repo?.license)
    }

    @Test
    fun `getRepositories returns failure when API call throws exception`() = runTest {
        // Arrange
        val errorMessage = "Network Error"
        coEvery { apiService.searchRepositories(any(), any(), any()) } throws Exception(errorMessage)

        // Act
        val result = repository.getRepositories("kotlin")

        // Assert
        assertTrue(result.isFailure)
        assertEquals(errorMessage, result.exceptionOrNull()?.message)
    }
}