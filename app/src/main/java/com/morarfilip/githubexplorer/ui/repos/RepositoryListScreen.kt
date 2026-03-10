package com.morarfilip.githubexplorer.ui.repos

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.morarfilip.githubexplorer.core.model.Repository
import com.morarfilip.githubexplorer.ui.repos.components.RepositoryCard
import com.morarfilip.githubexplorer.ui.theme.GithubExplorerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepositoryListScreen(
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    onRepoClick: (Repository) -> Unit,
    viewModel: RepositoryListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    RepositoryListContent(
        uiState = uiState,
        searchQuery = searchQuery,
        sharedTransitionScope = sharedTransitionScope,
        animatedContentScope = animatedContentScope,
        onIntent = viewModel::onIntent,
        onRepoClick = onRepoClick,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepositoryListContent(
    uiState: RepositoryUiState,
    searchQuery: String,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    onIntent: (RepositoryListIntent) -> Unit,
    onRepoClick: (Repository) -> Unit,
) {
    val isRefreshing = uiState is RepositoryUiState.Loading

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("Github Repositories")
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    scrolledContainerColor = Color.Unspecified,
                    navigationIconContentColor = Color.Unspecified,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    actionIconContentColor = Color.Unspecified
                )
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = {
                    onIntent(RepositoryListIntent.SearchQueryChanged(it))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                placeholder = {
                    Text(text = "Search by language (e.g. Kotlin)")
                },
                singleLine = true
            )

            PullToRefreshBox(
                isRefreshing = isRefreshing,
                onRefresh = {
                    onIntent(RepositoryListIntent.RefreshTriggered)
                },
                modifier = Modifier.fillMaxSize()
            ) {
                when (uiState) {
                    is RepositoryUiState.Loading -> Box(modifier = Modifier.fillMaxSize()) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .testTag("loading_indicator")
                        )
                    }

                    is RepositoryUiState.Error -> Box(modifier = Modifier.fillMaxSize()) {
                        Text(
                            text = uiState.message,
                            modifier = Modifier.align(alignment = Alignment.Center),
                            color = Color.Red
                        )
                    }

                    is RepositoryUiState.Success -> {
                        if (uiState.repos.isEmpty()) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "No repositories found for \"$searchQuery\"",
                                    style = MaterialTheme.typography.bodyLarge,
                                    textAlign = TextAlign.Center
                                )
                            }
                        } else {
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(2),
                                contentPadding = PaddingValues(8.dp),
                                modifier = Modifier.fillMaxSize()
                            ) {
                                items(uiState.repos) { repo ->
                                    RepositoryCard(
                                        repo = repo,
                                        sharedTransitionScope = sharedTransitionScope,
                                        animatedContentScope = animatedContentScope,
                                        onClick = {
                                            onIntent(RepositoryListIntent.RepositoryClicked(repo))
                                            onRepoClick(repo)
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Preview(
    showBackground = true,
    device = "spec:width=411dp,height=891dp",
    name = "Phone - Light"
)
@Preview(
    showBackground = true,
    device = "spec:width=411dp,height=891dp",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES,
    name = "Phone - Dark"
)
@Preview(
    showBackground = true,
    device = "spec:width=1280dp,height=800dp,orientation=landscape",
    name = "Tablet - Light"
)
@Preview(
    showBackground = true,
    device = "spec:width=1280dp,height=800dp,orientation=landscape",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES,
    name = "Tablet - Dark"
)
@Composable
fun PreviewRepositoryListContent() {
    val mockRepos = List(8) { index ->
        Repository(
            id = index.toLong(),
            name = if (index % 2 == 0) "Compose-Samples" else "Accompanist",
            ownerName = if (index % 2 == 0) "android" else "google",
            ownerAvatarUrl = "",
            description = "A collection of Jetpack Compose samples and libraries for building modern UIs.",
            stars = 25000,
            forks = 5000,
            watchers = 1200,
            openIssues = 42,
            license = "Apache 2.0",
            lastUpdated = "2026-03-10T10:00:00Z",
            language = "Kotlin"
        )
    }

    GithubExplorerTheme {
        SharedTransitionLayout {
            AnimatedContent(targetState = true, label = "list_preview") { target ->
                if (target) {
                    Surface(color = MaterialTheme.colorScheme.background) {
                        RepositoryListContent(
                            uiState = RepositoryUiState.Success(mockRepos),
                            searchQuery = "Kotlin",
                            sharedTransitionScope = this@SharedTransitionLayout,
                            animatedContentScope = this@AnimatedContent,
                            onIntent = {},
                            onRepoClick = {}
                        )
                    }
                }
            }
        }
    }
}