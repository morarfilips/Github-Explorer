package com.morarfilip.githubexplorer.ui.repos.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.morarfilip.githubexplorer.core.model.Repository
import com.morarfilip.githubexplorer.ui.theme.GithubExplorerTheme
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class RepositoryCardTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val mockRepo = Repository(
        1L,
        "Test-Repo",
        "This is a test description",
        100,
        20,
        50,
        5,
        "2026-03-09T15:54:11Z",
        "test_owner",
        "https://example.com/avatar.png",
        "Kotlin",
        "MIT"
    )

    // Helper to provide the required transition environment for the card
    @Composable
    private fun TestCardWrapper(onClick: () -> Unit = {}) {
        GithubExplorerTheme {
            SharedTransitionLayout {
                AnimatedContent(targetState = true, label = "card_test") { target ->
                    if (target) {
                        RepositoryCard(
                            repo = mockRepo,
                            sharedTransitionScope = this@SharedTransitionLayout,
                            animatedContentScope = this@AnimatedContent,
                            onClick = onClick
                        )
                    }
                }
            }
        }
    }

    @Test
    fun repositoryCard_displaysKeyInformation() {
        // Arrange & Act
        composeTestRule.setContent {
            TestCardWrapper()
        }

        // Assert
        composeTestRule.onNodeWithText("Test-Repo").assertIsDisplayed()
        composeTestRule.onNodeWithText("This is a test description").assertIsDisplayed()

        composeTestRule.onNodeWithContentDescription("Avatar of Test-Repo owner").assertIsDisplayed()
    }

    @Test
    fun repositoryCard_triggersOnClick() {
        // Arrange
        var clicked = false

        // Act
        composeTestRule.setContent {
            TestCardWrapper {
                clicked = true
            }
        }

        composeTestRule.onNodeWithText("Test-Repo").performClick()

        // Assert
        assertTrue("Click listener was not triggered", clicked)
    }
}