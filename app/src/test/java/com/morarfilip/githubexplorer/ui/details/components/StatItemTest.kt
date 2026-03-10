package com.morarfilip.githubexplorer.ui.details.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class StatItemTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun displaysLabelAndValueCorrectly() {
        // Arrange & Act
        composeTestRule.setContent {
            StatItem(label = "Forks", value = "123")
        }

        // Assert
        composeTestRule.onNodeWithText("Forks").assertIsDisplayed()
        composeTestRule.onNodeWithText("123").assertIsDisplayed()
    }
}