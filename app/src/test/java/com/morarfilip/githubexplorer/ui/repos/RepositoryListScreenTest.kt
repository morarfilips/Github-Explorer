package com.morarfilip.githubexplorer.ui.repos

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextReplacement
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeDown
import com.morarfilip.githubexplorer.core.model.Repository
import com.morarfilip.githubexplorer.ui.theme.GithubExplorerTheme
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
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

    // Helper to provide the necessary transition scopes for the list
    @Composable
    private fun TestListContentWrapper(
        uiState: RepositoryUiState,
        searchQuery: String = "kotlin",
        onIntent: (RepositoryListIntent) -> Unit = {}
    ) {
        GithubExplorerTheme {
            SharedTransitionLayout {
                AnimatedContent(targetState = true, label = "list_test") { target ->
                    if (target) {
                        RepositoryListContent(
                            uiState = uiState,
                            searchQuery = searchQuery,
                            onIntent = onIntent,
                            onRepoClick = {},
                            sharedTransitionScope = this@SharedTransitionLayout,
                            animatedContentScope = this@AnimatedContent
                        )
                    }
                }
            }
        }
    }

    @Test
    fun repositoryList_displaysItemsInGrid_onSuccess() {
        // Arrange
        val mockRepos = listOf(mockRepo)

        // Act
        composeTestRule.setContent {
            TestListContentWrapper(uiState = RepositoryUiState.Success(mockRepos))
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
            TestListContentWrapper(uiState = RepositoryUiState.Loading, searchQuery = "")
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
            TestListContentWrapper(uiState = RepositoryUiState.Error(errorMsg))
        }

        // Assert
        composeTestRule.onNodeWithText(errorMsg).assertIsDisplayed()
    }

    @Test
    fun repositoryList_displaysEmptyState_onNoResults() {
        // Arrange & Act
        composeTestRule.setContent {
            TestListContentWrapper(uiState = RepositoryUiState.Success(emptyList()), searchQuery = "xyz123")
        }

        // Assert
        composeTestRule.onNodeWithText("No repositories found", substring = true).assertIsDisplayed()
    }
    @Test
    fun searchField_triggersIntentOnValueChange() {
        // Arrange
        var capturedIntent: RepositoryListIntent? = null

        // Act
        composeTestRule.setContent {
            TestListContentWrapper(
                uiState = RepositoryUiState.Success(emptyList()),
                onIntent = {
                    capturedIntent = it
                }
            )
        }

        composeTestRule.onNodeWithText("kotlin").performTextReplacement("kotlin!")

        // Assert
        assertTrue(capturedIntent is RepositoryListIntent.SearchQueryChanged)
        assertEquals("kotlin!", (capturedIntent as RepositoryListIntent.SearchQueryChanged).query)
    }

    @Test
    fun pullToRefresh_triggersRefreshIntent() {
        // Arrange
        var capturedIntent: RepositoryListIntent? = null

        // Act
        composeTestRule.setContent {
            TestListContentWrapper(
                uiState = RepositoryUiState.Success(listOf(mockRepo)),
                onIntent = {
                    capturedIntent = it
                }
            )
        }

        composeTestRule
            .onNodeWithText("Compose-App")
            .performTouchInput {
                swipeDown(
                    startY = 0f,
                    endY = 500f,
                    durationMillis = 200
                )
            }

        // Assert
        assertEquals(RepositoryListIntent.RefreshTriggered, capturedIntent)
    }
}