package com.morarfilip.githubexplorer.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.morarfilip.githubexplorer.ui.repos.RepositoryListScreen

@Composable
fun NavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = RepoListRoute
    ) {
        composable<RepoListRoute> {
            RepositoryListScreen(
                onRepoClick = { repo ->
                    navController.navigate(
                        RepoDetailRoute(
                            id = repo.id,
                            name = repo.name,
                            ownerAvatarUrl = repo.ownerAvatarUrl,
                            description = repo.description,
                            stars = repo.stars,
                            forks = repo.forks,
                            updatedAt = repo.lastUpdated,
                            language = repo.language
                        )
                    )
                }
            )
        }

        composable<RepoDetailRoute> { backStackEntry ->
            val detailData = backStackEntry.toRoute<RepoDetailRoute>()
            RepositoryDetailScreen(
                detail = detailData,
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}

@Composable
fun RepositoryDetailScreen(detail: RepoDetailRoute, onBack: () -> Boolean) {
    TODO("Not yet implemented")
}