package com.morarfilip.githubexplorer.core.networking.mapper

import com.morarfilip.githubexplorer.core.networking.dto.GithubRepoDto
import com.morarfilip.githubexplorer.core.networking.dto.OwnerDto
import org.junit.Test

class GithubRepositoryMapperTest {

    @Test
    fun `map DTO to Domain handles null description and language`() {
        // Arrange
        val dto = GithubRepoDto(
            id = 123,
            name = "Modern-App",
            description = null,
            stars = 99,
            forks = 10,
            updatedAt = "2026-03-08T12:00:00Z",
            owner = OwnerDto(avatarUrl = "https://avatar.com/1"),
            language = null
        )

        // Act
        val domain = dto.toDomain()

        // Assert
        assert(domain.description == "No description provided")
        assert(domain.language == "Unknown")
        assert(domain.name == "Modern-App")
    }

    @Test
    fun `map DTO to Domain preserves correct values`() {
        // Arrange
        val dto = GithubRepoDto(
            id = 1,
            name = "Kotlin-Rocks",
            description = "A great repo",
            stars = 500,
            forks = 20,
            updatedAt = "yesterday",
            owner = OwnerDto("https://img.com"),
            language = "Kotlin"
        )

        // Act
        val domain = dto.toDomain()

        // Assert
        assert(domain.stars == 500)
        assert(domain.ownerAvatarUrl == "https://img.com")
    }
}