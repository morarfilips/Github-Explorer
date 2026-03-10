package com.morarfilip.githubexplorer.ui.details

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import com.morarfilip.githubexplorer.ui.navigation.RepositoryDetailRoute
import com.morarfilip.githubexplorer.ui.theme.GithubExplorerTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config


@RunWith(RobolectricTestRunner::class)
class RepositoryDetailScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val mockDetail = RepositoryDetailRoute(
        1,
        "Test-Repo",
        "morarfilip",
        "https://url.com",
        "A very cool repository description for testing.",
        100,
        50,
        80,
        5,
        "MIT",
        "2026-03-09T15:54:11Z",
        "Kotlin"
    )

    // Helper to wrap the screen in the required scopes for testing
    @Composable
    private fun TestDetailScreenWrapper(onBack: () -> Unit = {}) {
        GithubExplorerTheme {
            SharedTransitionLayout {
                AnimatedContent(
                    targetState = true,
                    label = "test"
                ) { target ->
                    if (target) {
                        RepositoryDetailScreen(
                            detail = mockDetail,
                            sharedTransitionScope = this@SharedTransitionLayout,
                            animatedContentScope = this@AnimatedContent,
                            onBack = onBack
                        )
                    }
                }
            }
        }
    }

    @Test
    fun topAppBarShowsCorrectTitleAndBackButton() {
        // Arrange & Act
        composeTestRule.setContent {
            TestDetailScreenWrapper()
        }

        // Assert
        composeTestRule.onAllNodesWithText("Test-Repo").onFirst().assertIsDisplayed()

        composeTestRule.onNodeWithContentDescription("Back").assertIsDisplayed()
    }

    @Test
    @Config(qualifiers = "w360dp-h800dp") // Force Phone Portrait
    fun phonePortrait_showsVerticalLayout() {
        // Arrange & Act
        composeTestRule.setContent {
            TestDetailScreenWrapper()
        }

        // Assert
        composeTestRule.onNodeWithText("Owner").assertIsDisplayed()
        composeTestRule.onNodeWithText("morarfilip").assertIsDisplayed()
        composeTestRule.onNodeWithText("MIT").assertIsDisplayed()
    }

    @Test
    @Config(qualifiers = "w1280dp-h800dp") // Force Tablet Landscape
    fun tabletLandscape_showsExpandedLayout() {
        // Arrange & Act
        composeTestRule.setContent {
            TestDetailScreenWrapper()
        }

        // Assert
        composeTestRule.onNodeWithText("100").assertIsDisplayed() // Stars
        composeTestRule.onNodeWithText("50").assertIsDisplayed()  // Forks
        composeTestRule.onNodeWithText("80").assertIsDisplayed()  // Watchers
        composeTestRule.onNodeWithText("5").assertIsDisplayed()   // Issues
    }
}