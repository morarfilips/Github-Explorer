package com.morarfilip.githubexplorer.core.model

data class Repository(
    val id: Long,
    val name: String,
    val description: String,
    val stars: Int,
    val forks: Int,
    val lastUpdated: String,
    val ownerAvatarUrl: String,
    val language: String
)