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

    @Test
    fun repositoryCard_displaysAllInformation() {
        // Arrange
        val mockRepo = Repository(
            1,
            "Test-Repo",
            "This is a test description",
            100,
            20,
            "2026-03-08",
            "https://example.com/avatar.png",
            "Kotlin"
        )

        // Act
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
        val mockRepo = Repository(
            1,
            "Name",
            "Desc",
            0,
            0,
            "",
            "",
            ""
        )

        composeTestRule.setContent {
            RepositoryCard(
                repo = mockRepo,
                onClick = {
                    clicked = true
                }
            )
        }

        // Act
        composeTestRule.onNodeWithText("Name").performClick()

        // Assert
        assertTrue("Click listener was not triggered", clicked)
    }
}