package com.morarfilip.githubexplorer.ui.details.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.morarfilip.githubexplorer.ui.navigation.RepositoryDetailRoute
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class DetailBodyTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val mockFullDetail = RepositoryDetailRoute(
        1,
        "Advanced-Kotlin",
        "JetBrains",
        "https://avatar.url",
        "Deep dive into Coroutines",
        12000,
        800,
        3500,
        15,
        "Apache 2.0",
        "2026-03-09T15:54:11Z",
        "Kotlin"
    )

    @Test
    fun displaysAllRepoInfo_includingNewStatsAndLicense() {
        // Arrange & Act
        composeTestRule.setContent {
            DetailBody(detail = mockFullDetail)
        }

        // Assert
        composeTestRule.onNodeWithText("Advanced-Kotlin").assertIsDisplayed()
        composeTestRule.onNodeWithText("Kotlin").assertIsDisplayed()

        composeTestRule.onNodeWithText("12000").assertIsDisplayed()
        composeTestRule.onNodeWithText("800").assertIsDisplayed()
        composeTestRule.onNodeWithText("3500").assertIsDisplayed()
        composeTestRule.onNodeWithText("15").assertIsDisplayed()

        composeTestRule.onNodeWithText("JetBrains").assertIsDisplayed()
        composeTestRule.onNodeWithText("Apache 2.0").assertIsDisplayed()

        composeTestRule.onNodeWithText("Deep dive into Coroutines").assertIsDisplayed()
    }

    @Test
    fun showsFallbackWhenDescriptionIsEmpty() {
        // Arrange
        val emptyDescMock = mockFullDetail.copy(description = "")

        // Act
        composeTestRule.setContent {
            DetailBody(detail = emptyDescMock)
        }

        // Assert
        composeTestRule.onNodeWithText("No description available for this repository.").assertIsDisplayed()
    }

    @Test
    fun showsFallbackWhenLicenseIsEmpty() {
        // Arrange
        val noLicenseMock = mockFullDetail.copy(license = "")

        // Act
        composeTestRule.setContent {
            DetailBody(detail = noLicenseMock)
        }

        // Assert
        composeTestRule.onNodeWithText("No License").assertIsDisplayed()
    }
}