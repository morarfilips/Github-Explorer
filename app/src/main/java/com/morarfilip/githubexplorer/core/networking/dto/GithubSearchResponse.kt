package com.morarfilip.githubexplorer.core.networking.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GithubSearchResponse(
    @SerialName("items") val items: List<GithubRepositoryDto>
)
