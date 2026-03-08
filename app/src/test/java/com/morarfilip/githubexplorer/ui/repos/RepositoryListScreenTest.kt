package com.morarfilip.githubexplorer.ui.repos

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
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

    @Test
    fun repositoryList_displaysItemsInGrid() {
        // Arrange
        val mockRepos = listOf(
            Repository(
                1,
                "Compose-App",
                "Desc",
                500,
                50,
                "2026",
                "url",
                "Kotlin"
            )
        )

        // Act
        composeTestRule.setContent {
            RepositoryListContent(
                uiState = RepoUiState.Success(mockRepos),
                searchQuery = "",
                onQueryChange = {},
                onRepoClick = {}
            )
        }

        // Assert
        composeTestRule.onNodeWithText("Compose-App").assertIsDisplayed()
        composeTestRule.onNodeWithText("Desc").assertIsDisplayed()
    }
}