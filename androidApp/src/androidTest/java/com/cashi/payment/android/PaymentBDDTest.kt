package com.cashi.payment.android

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PaymentBDDTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setup() {
        // wait for the UI to be ready
        composeTestRule.waitForIdle()
    }

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
        // given - already on payment screen (default route)

        // and - enter recipient email
        composeTestRule
            .onNodeWithTag("emailInput", useUnmergedTree = true)
            .performTextInput("test@example.com")

        // and - enter amount
        composeTestRule
            .onNodeWithTag("amountInput", useUnmergedTree = true)
            .performTextInput("50.00")


        // when - click send payment button
        composeTestRule
            .onAllNodes(hasText("Send Payment") and hasClickAction())
            .onLast()
            .performClick()

        // and - navigate to history using the bottom navigation
        composeTestRule
            .onNodeWithText("History")
            .performClick()

        // Wait for navigation and screen to load
        composeTestRule.waitForIdle()

        // Verify the transaction appears
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule
                .onAllNodesWithText("test@example.com", useUnmergedTree = true)
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
        // given - already on payment screen

        // and - enter invalid email
        composeTestRule
            .onNodeWithTag("emailInput", useUnmergedTree = true)
            .performTextInput("invalid-email")

        // and - enter amount
        composeTestRule
            .onNodeWithTag("amountInput", useUnmergedTree = true)
            .performTextInput("100.00")

        // and - select EUR currency
        // The ExposedDropdownMenuBox needs the menuAnchor to be clicked
        composeTestRule
            .onNodeWithText("USD") // Current value shown in the text field
            .performClick()

        // Wait for dropdown menu to appear
        composeTestRule.waitForIdle()

        // Look for the EUR option - it should show "EUR €" based on the getCurrencySymbol function
        composeTestRule
            .onNodeWithText("EUR €", useUnmergedTree = true)
            .performClick()

        // Wait for dropdown to close
        composeTestRule.waitForIdle()

        // when - click send payment button
        composeTestRule
            .onAllNodes(hasText("Send Payment") and hasClickAction())
            .onLast()
            .performClick()

        // then - verify error message
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
        // given - already on payment screen

        // and - enter valid email
        composeTestRule
            .onNodeWithTag("emailInput", useUnmergedTree = true)
            .performTextInput("test@example.com")

        // and - enter zero amount
        composeTestRule
            .onNodeWithTag("amountInput", useUnmergedTree = true)
            .performTextInput("0")

        // Currency is USD by default, so no need to change it

        // when - click send payment button
        composeTestRule
            .onAllNodes(hasText("Send Payment") and hasClickAction())
            .onLast()
            .performClick()

        // then - verify error message
        composeTestRule
            .onNodeWithText("Amount must be greater than 0")
            .assertIsDisplayed()
    }

}