package com.morarfilip.githubexplorer.core.networking.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OwnerDto(
    @SerialName("avatar_url") val avatarUrl: String
)