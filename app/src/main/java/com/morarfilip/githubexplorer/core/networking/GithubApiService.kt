package com.morarfilip.githubexplorer.core.networking

import com.morarfilip.githubexplorer.core.networking.dto.GithubSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface GithubApiService {
    @GET("search/repositories")
    suspend fun searchRepositories(
        @Query("q") query: String,
        @Query("sort") sort: String? = null,
        @Query("order") order: String = "desc"
    ): GithubSearchResponse
}