package com.morarfilip.githubexplorer.core.model

data class Repository(
    val id: Long,
    val name: String,
    val description: String,
    val stars: Int,
    val forks: Int,
    val watchers: Int,
    val openIssues: Int,
    val lastUpdated: String,
    val ownerName: String,
    val ownerAvatarUrl: String,
    val language: String,
    val license: String,
)