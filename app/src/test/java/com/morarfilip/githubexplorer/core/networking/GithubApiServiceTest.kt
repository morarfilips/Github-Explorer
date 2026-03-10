package com.morarfilip.githubexplorer.core.networking

import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

class GithubApiServiceTest {
    private lateinit var server: MockWebServer
    private lateinit var api: GithubApiService

    private val jsonConfig = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    @Before
    fun setup() {
        server = MockWebServer()
        val contentType = "application/json".toMediaType()

        api = Retrofit.Builder()
            .baseUrl(server.url("/"))
            .addConverterFactory(jsonConfig.asConverterFactory(contentType))
            .build()
            .create(GithubApiService::class.java)
    }

    @Test
    fun `searchRepositories parses full JSON schema correctly`() = runTest {
        // Arrange
        val mockJsonResponse = """
        {
            "items": [
                {
                    "id": 1,
                    "name": "GithubProject",
                    "description": "A cool repo",
                    "stargazers_count": 100,
                    "forks_count": 50,
                    "watchers_count": 75,
                    "open_issues_count": 3,
                    "updated_at": "2026-03-09T15:54:11Z",
                    "language": "Kotlin",
                    "owner": { 
                        "login": "morarfilip",
                        "avatar_url": "https://url.com" 
                    },
                    "license": {
                        "name": "MIT License"
                    }
                }
            ]
        }""".trimIndent()

        server.enqueue(MockResponse().setBody(mockJsonResponse).setResponseCode(200))

        // Act
        val response = api.searchRepositories("kotlin")

        // Assert
        val item = response.items.first()
        assertEquals("GithubProject", item.name)
        assertEquals(100, item.stars)
        assertEquals(75, item.watchers)
        assertEquals(3, item.openIssues)
        assertEquals("morarfilip", item.owner.login)
        assertEquals("MIT License", item.license?.name)
    }

    @After
    fun tearDown() {
        server.shutdown()
    }
}