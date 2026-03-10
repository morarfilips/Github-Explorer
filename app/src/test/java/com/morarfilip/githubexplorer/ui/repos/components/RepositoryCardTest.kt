package com.morarfilip.githubexplorer.ui.repos.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.morarfilip.githubexplorer.core.model.Repository
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

    @Test
    fun repositoryCard_displaysKeyInformation() {
        // Arrange & Act
        composeTestRule.setContent {
            RepositoryCard(
                repo = mockRepo,
                onClick = {}
            )
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
            RepositoryCard(
                repo = mockRepo,
                onClick = {
                    clicked = true
                }
            )
        }

        composeTestRule.onNodeWithText("Test-Repo").performClick()

        // Assert
        assertTrue("Click listener was not triggered", clicked)
    }
}