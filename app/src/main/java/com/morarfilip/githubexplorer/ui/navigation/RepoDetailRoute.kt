package com.morarfilip.githubexplorer.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
data class RepoDetailRoute(
    val id: Long,
    val name: String,
    val ownerAvatarUrl: String,
    val description: String,
    val stars: Int,
    val forks: Int,
    val updatedAt: String,
    val language: String
)