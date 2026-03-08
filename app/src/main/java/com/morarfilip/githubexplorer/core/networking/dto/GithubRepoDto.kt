package com.morarfilip.githubexplorer.core.networking.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GithubRepoDto(
    val id: Long,
    val name: String,
    val description: String?,
    @SerialName("stargazers_count") val stars: Int,
    @SerialName("forks_count") val forks: Int,
    @SerialName("updated_at") val updatedAt: String,
    val owner: OwnerDto,
    val language: String?
)