package com.morarfilip.githubexplorer.ui.details.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class InfoLabelTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun infoLabel_displaysLabelAndValue() {
        // Arrange
        val testLabel = "License"
        val testValue = "MIT"

        // Act
        composeTestRule.setContent {
            InfoLabel(
                label = testLabel,
                value = testValue
            )
        }

        // Assert
        composeTestRule.onNodeWithText(testLabel).assertIsDisplayed()
        composeTestRule.onNodeWithText(testValue).assertIsDisplayed()
    }
}