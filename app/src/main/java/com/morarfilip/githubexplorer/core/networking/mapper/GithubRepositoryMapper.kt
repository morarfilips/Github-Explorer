package com.morarfilip.githubexplorer.core.networking.mapper

import com.morarfilip.githubexplorer.core.model.Repository
import com.morarfilip.githubexplorer.core.networking.dto.GithubRepositoryDto

fun GithubRepositoryDto.toDomain(): Repository {
    return Repository(
        id = id,
        name = name,
        ownerName = owner.login,
        ownerAvatarUrl = owner.avatarUrl,
        description = description ?: "",
        stars = stars,
        forks = forks,
        watchers = watchers,
        openIssues = openIssues,
        lastUpdated = updatedAt,
        language = language ?: "Unknown",
        license = license?.name ?: "No License"
    )
}