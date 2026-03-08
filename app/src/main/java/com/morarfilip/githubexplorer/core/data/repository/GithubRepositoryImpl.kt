package com.morarfilip.githubexplorer.core.data.repository

import com.morarfilip.githubexplorer.core.model.Repository
import com.morarfilip.githubexplorer.core.networking.GithubApiService
import com.morarfilip.githubexplorer.core.networking.mapper.toDomain
import jakarta.inject.Inject
import jakarta.inject.Singleton

@Singleton
class GithubRepositoryImpl @Inject constructor(
    private val apiService: GithubApiService
) : GithubRepository {

    override suspend fun getRepositories(query: String, sort: String?): Result<List<Repository>> {
        return try {
            val finalQuery = query.ifBlank {
                "stars:>10000"
            }
            val response = apiService.searchRepositories(query = finalQuery, sort = sort)
            Result.success(response.items.map {
                it.toDomain()
            })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}