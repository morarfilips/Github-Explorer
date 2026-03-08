package com.morarfilip.githubexplorer.core.networking

import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

class GithubApiServiceTest {
    private lateinit var server: MockWebServer
    private lateinit var api: GithubApiService

    @Before
    fun setup() {
        server = MockWebServer()
        val json = Json { ignoreUnknownKeys = true }
        val contentType = "application/json".toMediaType()

        api = Retrofit.Builder()
            .baseUrl(server.url("/"))
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
            .create(GithubApiService::class.java)
    }

    @Test
    fun `searchRepositories parses JSON correctly`() = runTest {
        val mockJsonResponse = """
        {
            "items": [
                {
                    "id": 1,
                    "name": "GithubProject",
                    "description": "A cool repo",
                    "stargazers_count": 100,
                    "forks_count": 50,
                    "updated_at": "2026-03-08T12:00:00Z",
                    "language": "Kotlin",
                    "owner": { "avatar_url": "https://url.com" }
                }
            ]
        }""".trimIndent()

        server.enqueue(MockResponse().setBody(mockJsonResponse).setResponseCode(200))

        // Act
        val response = api.searchRepositories("kotlin")

        // Assert
        val item = response.items.first()
        assert(item.name == "GithubProject")
        assert(item.stars == 100)
    }

    @After
    fun tearDown() {
        server.shutdown()
    }
}