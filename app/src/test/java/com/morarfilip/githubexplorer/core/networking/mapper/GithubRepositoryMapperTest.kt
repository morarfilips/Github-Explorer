package com.morarfilip.githubexplorer.core.networking.mapper

import com.morarfilip.githubexplorer.core.networking.dto.GithubRepositoryDto
import com.morarfilip.githubexplorer.core.networking.dto.LicenseDto
import com.morarfilip.githubexplorer.core.networking.dto.OwnerDto
import org.junit.Assert.assertEquals
import org.junit.Test

class GithubRepositoryMapperTest {

    @Test
    fun `map DTO to Domain handles null values gracefully`() {
        // Arrange
        val dto = GithubRepositoryDto(
            123L,
            "Modern-App",
            null,
            99,
            10,
            5,
            2,
            "2026-03-08T12:00:00Z",
            OwnerDto(
                "dev_user",
                "https://avatar.com/1"
            ),
            null,
            null
        )

        // Act
        val domain = dto.toDomain()

        // Assert
        assertEquals("", domain.description)
        assertEquals("Unknown", domain.language)
        assertEquals("No License", domain.license)
        assertEquals("dev_user", domain.ownerName)
    }

    @Test
    fun `map DTO to Domain preserves all detailed values`() {
        // Arrange
        val dto = GithubRepositoryDto(
            id = 1,
            name = "Kotlin-Rocks",
            description = "A great repo",
            stars = 500,
            forks = 20,
            watchers = 150,
            openIssues = 12,
            updatedAt = "2026-03-09T15:54:11Z",
            owner = OwnerDto(login = "kotlin_fan", avatarUrl = "https://img.com"),
            language = "Kotlin",
            license = LicenseDto(name = "MIT License")
        )

        // Act
        val domain = dto.toDomain()

        // Assert
        assertEquals(500, domain.stars)
        assertEquals(20, domain.forks)
        assertEquals(150, domain.watchers)
        assertEquals(12, domain.openIssues)
        assertEquals("kotlin_fan", domain.ownerName)
        assertEquals("https://img.com", domain.ownerAvatarUrl)
        assertEquals("MIT License", domain.license)
    }
}