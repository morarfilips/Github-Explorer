package com.morarfilip.githubexplorer.core.data.repository

import com.morarfilip.githubexplorer.core.model.Repository

interface GithubRepository {
    suspend fun getRepositories(
        query: String,
        sort: String? = null
    ): Result<List<Repository>>
}