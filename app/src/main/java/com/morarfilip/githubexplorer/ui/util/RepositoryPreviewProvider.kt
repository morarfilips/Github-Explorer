package com.morarfilip.githubexplorer.ui.util

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.morarfilip.githubexplorer.ui.navigation.RepositoryDetailRoute

class RepositoryPreviewProvider : PreviewParameterProvider<RepositoryDetailRoute> {
    override val values = sequenceOf(
        RepositoryDetailRoute(
            id = 1,
            name = "Advanced-Kotlin",
            ownerName = "JetBrains",
            ownerAvatarUrl = "https://avatars.githubusercontent.com/u/1?v=4",
            description = "A deep dive into Kotlin Coroutines, Flow, and Structured Concurrency for senior developers.",
            stars = 12500,
            forks = 850,
            watchers = 4500,
            openIssues = 12,
            license = "Apache 2.0",
            updatedAt = "2026-03-09T15:54:11Z",
            language = "Kotlin"
        ),
        RepositoryDetailRoute(
            id = 2,
            name = "Very-Long-Repository-Name-For-Testing-Ellipsis",
            ownerName = "long_username_test",
            ownerAvatarUrl = "",
            description = "This is a very long description intended to test how the UI handles overflow and scrolling on smaller devices.",
            stars = 10,
            forks = 2,
            watchers = 1,
            openIssues = 0,
            license = "MIT License",
            updatedAt = "2026-03-09T10:00:00Z",
            language = "Markdown"
        )
    )
}