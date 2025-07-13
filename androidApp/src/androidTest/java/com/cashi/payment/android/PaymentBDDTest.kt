package com.cashi.payment.android

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class PaymentBDDTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    /**
     * Scenario: User sends valid payment
     * Given I am on the payment screen
     * And I enter recipient email "test@example.com"
     * And I enter amount "50.00"
     * And I select currency "USD"
     * When I click send payment button
     * Then I should see success message "Payment sent successfully"
     * And the payment should appear in transaction history
     */
    @Test
    fun scenario_UserSendsValidPayment() {
        // given
        composeTestRule.waitForIdle()

        // and
        composeTestRule
            .onNodeWithContentDescription("Recipient Email")
            .performTextInput("test@example.com")

        // and
        composeTestRule
            .onNodeWithContentDescription("Amount")
            .performTextInput("50.00")


        // when
        composeTestRule
            .onAllNodesWithText("Send Payment")
            .filter(hasClickAction())
            .onFirst()
            .performClick()

        // then
        composeTestRule.waitUntil(timeoutMillis = 10000) {
            composeTestRule
                .onAllNodesWithText("Payment sent successfully", substring = true)
                .fetchSemanticsNodes().isNotEmpty()
        }

        // and
        composeTestRule
            .onNodeWithText("History")
            .performClick()

        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule
                .onAllNodesWithText("test@example.com")
                .fetchSemanticsNodes().isNotEmpty()
        }
    }

    /**
     * Scenario: User enters invalid email
     * Given I am on the payment screen
     * And I enter recipient email "invalid-email"
     * And I enter amount "100.00"
     * And I select currency "EUR"
     * When I click send payment button
     * Then I should see error message "Invalid email format"
     */
    @Test
    fun scenario_UserEntersInvalidEmail() {
        // guven
        composeTestRule.waitForIdle()

        // and
        composeTestRule
            .onNodeWithContentDescription("Recipient Email")
            .performTextInput("invalid-email")

        // and
        composeTestRule
            .onNodeWithContentDescription("Amount")
            .performTextInput("100.00")

        // and
        composeTestRule
            .onNode(hasText("Currency") and hasClickAction())
            .performClick()
        composeTestRule.waitForIdle()
        composeTestRule
            .onNodeWithText("EUR")
            .performClick()

        // when
        composeTestRule
            .onAllNodesWithText("Send Payment")
            .filter(hasClickAction())
            .onFirst()
            .performClick()

        // then
        composeTestRule
            .onNodeWithText("Invalid email format")
            .assertIsDisplayed()
    }

    /**
     * Scenario: User enters zero amount
     * Given I am on the payment screen
     * And I enter recipient email "test@example.com"
     * And I enter amount "0"
     * And I select currency "USD"
     * When I click send payment button
     * Then I should see error message "Amount must be greater than 0"
     */
    @Test
    fun scenario_UserEntersZeroAmount() {
        // given
        composeTestRule.waitForIdle()

        //and
        composeTestRule
            .onNodeWithContentDescription("Recipient Email")
            .performTextInput("test@example.com")

        // and
        composeTestRule
            .onNodeWithContentDescription("Amount")
            .performTextInput("0")


        // when
        composeTestRule
            .onAllNodesWithText("Send Payment")
            .filter(hasClickAction())
            .onFirst()
            .performClick()

        // then
        composeTestRule
            .onNodeWithText("Amount must be greater than 0")
            .assertIsDisplayed()
    }
}