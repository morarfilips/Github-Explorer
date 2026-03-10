package com.morarfilip.githubexplorer.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
data class RepositoryDetailRoute(
    val id: Long,
    val name: String,
    val ownerName: String,
    val ownerAvatarUrl: String,
    val description: String,
    val stars: Int,
    val forks: Int,
    val watchers: Int,
    val openIssues: Int,
    val license: String,
    val updatedAt: String,
    val language: String
)