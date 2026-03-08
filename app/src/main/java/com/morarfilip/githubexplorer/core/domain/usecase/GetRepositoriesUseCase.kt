package com.morarfilip.githubexplorer.core.domain.usecase

import com.morarfilip.githubexplorer.core.data.repository.GithubRepository
import com.morarfilip.githubexplorer.core.model.Repository
import jakarta.inject.Inject

class GetRepositoriesUseCase @Inject constructor(
    private val repository: GithubRepository
) {
    suspend operator fun invoke(
        query: String,
        sort: String? = null
    ): Result<List<Repository>> {
        return repository.getRepositories(query, sort)
    }
}