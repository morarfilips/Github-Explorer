package com.morarfilip.githubexplorer.ui.repos

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.morarfilip.githubexplorer.core.model.Repository
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class RepositoryListScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val mockRepo = Repository(
        1L,
        "Compose-App",
        "Modern Android UI",
        500,
        50,
        100,
        10,
        "2026-03-09T15:54:11Z",
        "google",
        "https://url.com",
        "Kotlin",
        "Apache 2.0"
    )

    @Test
    fun repositoryList_displaysItemsInGrid_onSuccess() {
        // Arrange
        val mockRepos = listOf(mockRepo)

        // Act
        composeTestRule.setContent {
            RepositoryListContent(
                uiState = RepoUiState.Success(mockRepos),
                searchQuery = "kotlin",
                onQueryChange = {},
                onRepoClick = {},
                onRefresh = {}
            )
        }

        // Assert
        composeTestRule.onNodeWithText("Compose-App").assertIsDisplayed()
        composeTestRule.onNodeWithText("Modern Android UI").assertIsDisplayed()

        composeTestRule.onNodeWithText("kotlin").assertIsDisplayed()
    }

    @Test
    fun repositoryList_displaysLoadingIndicator() {
        // Arrange & Act
        composeTestRule.setContent {
            RepositoryListContent(
                uiState = RepoUiState.Loading,
                searchQuery = "",
                onQueryChange = {},
                onRepoClick = {},
                onRefresh = {}
            )
        }

        // Assert
        composeTestRule.onNodeWithTag("loading_indicator").assertIsDisplayed()
    }

    @Test
    fun repositoryList_displaysErrorMessage_onFailure() {
        // Arrange
        val errorMsg = "Something went wrong"

        // Act
        composeTestRule.setContent {
            RepositoryListContent(
                uiState = RepoUiState.Error(errorMsg),
                searchQuery = "",
                onQueryChange = {},
                onRepoClick = {},
                onRefresh = {}
            )
        }

        // Assert
        composeTestRule.onNodeWithText(errorMsg).assertIsDisplayed()
    }

    @Test
    fun repositoryList_displaysEmptyState_onNoResults() {
        // Arrange & Act
        composeTestRule.setContent {
            RepositoryListContent(
                uiState = RepoUiState.Success(emptyList()),
                searchQuery = "xyz123",
                onQueryChange = {},
                onRepoClick = {},
                onRefresh = {}
            )
        }

        // Assert
        composeTestRule.onNodeWithText("No repositories found", substring = true).assertIsDisplayed()
    }
}