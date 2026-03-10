package com.morarfilip.githubexplorer.core.networking.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GithubRepositoryDto(
    val id: Long,
    val name: String,
    val description: String?,
    @SerialName("stargazers_count") val stars: Int,
    @SerialName("forks_count") val forks: Int,
    @SerialName("watchers_count") val watchers: Int,
    @SerialName("open_issues_count") val openIssues: Int,
    @SerialName("updated_at") val updatedAt: String,
    val owner: OwnerDto,
    val language: String?,
    val license: LicenseDto?
)