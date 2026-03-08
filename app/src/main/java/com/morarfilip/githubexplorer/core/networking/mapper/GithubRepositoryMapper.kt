package com.morarfilip.githubexplorer.core.networking.mapper

import com.morarfilip.githubexplorer.core.model.Repository
import com.morarfilip.githubexplorer.core.networking.dto.GithubRepoDto

fun GithubRepoDto.toDomain(): Repository {
    return Repository(
        id = id,
        name = name,
        description = description ?: "No description provided",
        stars = stars,
        forks = forks,
        lastUpdated = updatedAt,
        ownerAvatarUrl = owner.avatarUrl,
        language = language ?: "Unknown"
    )
}