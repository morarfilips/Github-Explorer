package com.morarfilip.githubexplorer.ui.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.morarfilip.githubexplorer.ui.details.RepositoryDetailScreen
import com.morarfilip.githubexplorer.ui.repos.RepositoryListScreen

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun NavGraph() {
    val navController = rememberNavController()

    SharedTransitionLayout {
        NavHost(
            navController = navController,
            startDestination = RepositoryListRoute
        ) {
            composable<RepositoryListRoute> {
                RepositoryListScreen(
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedContentScope = this@composable,
                    onRepoClick = { repo ->
                        navController.navigate(
                            RepositoryDetailRoute(
                                id = repo.id,
                                name = repo.name,
                                ownerName = repo.ownerName,
                                ownerAvatarUrl = repo.ownerAvatarUrl,
                                description = repo.description,
                                stars = repo.stars,
                                forks = repo.forks,
                                watchers = repo.watchers,
                                openIssues = repo.openIssues,
                                license = repo.license,
                                updatedAt = repo.lastUpdated,
                                language = repo.language
                            )
                        )
                    }
                )
            }

            composable<RepositoryDetailRoute> { backStackEntry ->
                val detailData = backStackEntry.toRoute<RepositoryDetailRoute>()
                RepositoryDetailScreen(
                    detail = detailData,
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedContentScope = this@composable,
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}